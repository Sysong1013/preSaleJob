package com.dangdang.dao.target;

import com.dangdang.modle.PreProductWarehouseStock;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by songyisong on 2017/11/21.
 */
public interface PostStockQueueMapper {

    @Insert("<script>" +
            "INSERT INTO poststock_queue (productid,storeid,op_num,orderid," +
            "op_source,op_reason,op_time,delmark,exe_time,op_type,warehouse_type," +
            "pre_arrival_date,stock_quantity_ts,stock_quantity_allot) " +
            "values" +
            "<foreach collection=\"sourceDataList\" item=\"item\" index=\"index\" separator=\",\">" +
            "(#{item.productId},#{item.warehouseId},#{item.preStockQuantity} ,-1," +
            "'API-ModifyProdStock','1002-Update PreStock',getdate()," +
            "0,getdate(),1,1,#{item.preArrivalDate,jdbcType=TIMESTAMP},0,0)" +
            "</foreach>" +
            "</script>")
    int insertPostStockQueue(@Param("sourceDataList") List<PreProductWarehouseStock> sourceDataList);
}
