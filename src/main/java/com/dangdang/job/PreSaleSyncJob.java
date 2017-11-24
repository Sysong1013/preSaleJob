package com.dangdang.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.service.DealDate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by songyisong on 2017/11/21.
 */
@Slf4j
public class PreSaleSyncJob implements SimpleJob {
    Logger errorlog = LoggerFactory.getLogger("application.error");
    @Resource
    DealDate dealDate;

    public void execute(ShardingContext shardingContext) {
        try {
            log.info("****PreSaleSyncJob Delaing Data! On ShardingItem:{}", shardingContext.getShardingItem());
            dealDate.syncData(shardingContext.getShardingItem(), shardingContext.getShardingTotalCount());
        } catch (Exception e) {
            errorlog.error("****PreSaleSyncJob Dealing Data Receive An Exception On ShardingItem:{}", shardingContext.getShardingItem(),e);

        }

    }
}
