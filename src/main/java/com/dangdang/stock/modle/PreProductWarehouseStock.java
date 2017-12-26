package com.dangdang.stock.modle;

import lombok.Data;

import java.util.Date;

/**
 * Created by songyisong on 2017/11/21.
 */
@Data
public class PreProductWarehouseStock {
    private long productId;
    private int warehouseId;
    private int preStockQuantity;
    private Date preArrivalDate;
}
