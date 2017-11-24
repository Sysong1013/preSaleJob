package com.dangdang.biz;

import com.dangdang.dao.source.SourceData;
import com.dangdang.modle.PreProductWarehouseStockModle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by songyisong on 2017/11/21.
 */
@Repository
public class PreProductWarehouseStockObj {
    @Resource
    private SourceData sourceData;

    public List<PreProductWarehouseStockModle> getData(int shardingItem,int shardingTotalCount)
    {
        return sourceData.getPreProductWarehouseStock(shardingItem,shardingTotalCount);
    }

    public int updateData(List<PreProductWarehouseStockModle> sourceDataList)
    {
        return sourceData.updatePreProductWarehouseStock(sourceDataList);
    }
}
