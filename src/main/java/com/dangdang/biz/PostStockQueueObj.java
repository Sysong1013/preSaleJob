package com.dangdang.biz;

import com.dangdang.dao.target.TargetData;
import com.dangdang.modle.PreProductWarehouseStockModle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by songyisong on 2017/11/21.
 */
@Repository
public class PostStockQueueObj {
    @Resource
    TargetData insertData;
    public int syncDate(List<PreProductWarehouseStockModle> sourceDataList) {
        return insertData.insertPostStockQueue(sourceDataList);
    }
}
