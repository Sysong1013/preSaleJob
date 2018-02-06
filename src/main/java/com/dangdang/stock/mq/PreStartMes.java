package com.dangdang.stock.mq;

import lombok.Data;

/**
 * Created by songyisong on 2018/2/5.
 */
@Data
public class PreStartMes {
    private long productId;
    private int warehouseId;

    public PreStartMes() {

    }

    public PreStartMes(long productId, int warehouseId) {
        this.productId = productId;
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return "PreStartMes{productId=" + productId + ",warehouseId=" + warehouseId + "}";
    }
}
