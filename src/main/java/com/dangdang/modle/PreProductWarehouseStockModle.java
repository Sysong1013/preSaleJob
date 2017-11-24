package com.dangdang.modle;

import lombok.Data;

import java.util.Date;

/**
 * Created by songyisong on 2017/11/21.
 */
@Data
public class PreProductWarehouseStockModle {
    long productId;
    int warehouseId;
    int preStockQuantity;
    Date preArrivalDate;
}
