package com.dangdang.stock.service;

import com.dangdang.stock.dao.source.PreProductWarehouseStockMapper;
import com.dangdang.stock.dao.target.PostStockQueueMapper;
import com.dangdang.stock.modle.PreProductWarehouseStock;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by songyisong on 2017/11/21.
 */
@Slf4j
@Repository
public class PreSaleSyncJobService {
    private static final Logger errorlog = LoggerFactory.getLogger("application.error");
    @Resource
    private PreProductWarehouseStockMapper preProductWarehouseStockMapper;
    @Resource
    private PostStockQueueMapper postStockQueueMapper;

    public List<PreProductWarehouseStock> getData(int shardingItem, int shardingTotalCount) {
        //从预售表获取待同步数据
        try {
            List<PreProductWarehouseStock> sourceDataList = preProductWarehouseStockMapper.getPreProductWarehouseStock(shardingItem, shardingTotalCount);
            if (sourceDataList.size() == 0) {
                log.info("No Data To Sync On ShardingItem {}", shardingItem);
                return sourceDataList;
            }
            log.info("Get {} PerSaleDatas To Sync On ShardingItem {}", sourceDataList.size(), shardingItem);
            return sourceDataList;
        }catch (Exception e){
            errorlog.error("query data receive an exception e:",e);
            return null;
        }
    }

    public void dealData(int shardingItem, List<PreProductWarehouseStock> sourceDataList) {
        try {
            Long startTime = System.currentTimeMillis();
            //将数据插入poststock_queue，库存同步作业会同步至主表，预售生效
            int insertResult = postStockQueueMapper.insertPostStockQueue(sourceDataList);
            if (insertResult != sourceDataList.size()) {
                errorlog.error("Sql Exception :{} Datas Sync Failed! On ShardingItem {}", sourceDataList.size() - insertResult, shardingItem);
            }
            //更新预售表update_status=1避免重复处理
            int updateResult = preProductWarehouseStockMapper.updatePreProductWarehouseStock(sourceDataList);
            Long endTime = System.currentTimeMillis();
            log.info("{} datas to sync ,success sync {} datas , used {}ms",
                    sourceDataList.size(), insertResult, endTime - startTime);
        } catch (Exception e) {
            errorlog.error("Receive An Exception On ShardingItem {} :", shardingItem, e);
        }
    }
}
