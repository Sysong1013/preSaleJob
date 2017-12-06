package com.dangdang.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.modle.PreProductWarehouseStock;
import com.dangdang.service.PreSaleSyncJobService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by songyisong on 2017/11/21.
 */
@Slf4j
public class PreSaleSyncJob implements DataflowJob<PreProductWarehouseStock> {
    Logger errorlog = LoggerFactory.getLogger("application.error");
    @Resource
    PreSaleSyncJobService preSaleSyncJobService;

    public List<PreProductWarehouseStock> fetchData(ShardingContext shardingContext) {
        try {
            log.info("**** PreSaleSyncJob fetching data! shardingItem:{}", shardingContext.getShardingItem());
            return preSaleSyncJobService.getData(shardingContext.getShardingItem(), shardingContext.getShardingTotalCount());
        } catch (Exception e) {
            errorlog.error("****PreSaleSyncJob get source data failed! shardingItem:{}", shardingContext.getShardingItem(), e);
        }
        return null;
    }

    public void processData(ShardingContext shardingContext, List<PreProductWarehouseStock> sourcedata) {
        try {
            log.info("****PreSaleSyncJob processing data! shardingItem:{}", shardingContext.getShardingItem());
            preSaleSyncJobService.dealData(shardingContext.getShardingItem(), sourcedata);
        } catch (Exception e) {
            errorlog.error("****PreSaleSyncJob deal data failed! shardingItem:{}", shardingContext.getShardingItem(), e);
        }
    }
}
