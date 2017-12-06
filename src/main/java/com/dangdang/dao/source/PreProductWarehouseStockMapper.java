package com.dangdang.dao.source;

import com.dangdang.modle.PreProductWarehouseStock;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by songyisong on 2017/11/21.
 */
@Repository
public interface PreProductWarehouseStockMapper {
    //从预售表查询是预售(warehouse_type=1)&&未同步至主表(update_status=0)&&到了预售开始时间(event_start_time<=now)&&是普通预售的数据(pre_stock_type=0)
    @Results({
            @Result(id = true, column = "product_id", property = "productId"),
            @Result(column = "warehouse_id", property = "warehouseId"),
            @Result(column = "pre_stock_quantity", property = "preStockQuantity"),
            @Result(column = "pre_arrival_date", property = "preArrivalDate")
    })
    @Select("SELECT top 500 product_id,warehouse_id,pre_stock_quantity,pre_arrival_date " +
            "FROM pre_product_warehouse_stock" +
            " WHERE product_id%#{shardingTotalCount}=#{shardingItem} " +
            "AND warehouse_type=1 AND (pre_stock_type=0 OR pre_stock_type IS NULL)" +
            "AND (update_status=0 OR update_status IS NULL ) " +
            "AND datediff(ms,ISNULL(event_start_time,getdate()),getdate())>=0 ORDER BY last_changed_date ASC")
    List<PreProductWarehouseStock> getPreProductWarehouseStock(@Param("shardingItem") int shardingItem,
                                                               @Param("shardingTotalCount") int shardingTotalCount);

    //更新维度：品+仓+pre_stock_type+warehouse_type
    @Update("<script>" +
            "<foreach collection=\"sourceDataList\" item=\"item\" index=\"index\">" +
            "UPDATE pre_product_warehouse_stock set update_status=1,last_changed_date=getdate()" +
            " WHERE" +
            " product_id=#{item.productId} AND warehouse_id=#{item.warehouseId}" +
            " AND (pre_stock_type=0 OR pre_stock_type IS NULL) AND warehouse_type=1" +
            "</foreach>" +
            "</script>")
    int updatePreProductWarehouseStock(@Param("sourceDataList") List<PreProductWarehouseStock> sourceDataList);
}
