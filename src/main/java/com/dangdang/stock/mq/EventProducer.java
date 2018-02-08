package com.dangdang.stock.mq;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.dangdang.stock.modle.PreProductWarehouseStock;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by songyisong on 2018/2/5.
 */
@Component
@Slf4j
public class EventProducer {
    private static final Logger mqlog = LoggerFactory.getLogger("sendMes");
    private static final Logger errorlog = LoggerFactory.getLogger("application.error");
    @Value("${producerGroup}")
    private String producerGroup;
    @Value("${nameServer}")
    private String nameServer;
    @Value("${topic}")
    private String topic;
    @Value("${tag}")
    private String tag;
    private DefaultMQProducer producer;
    //静态-非静态-构造器-@Value-@PostConstruct
    @PostConstruct
    public void start() {
        try {
            producer = new DefaultMQProducer(producerGroup);
            producer.setNamesrvAddr(nameServer);
            producer.start();
        } catch (Exception e) {
            errorlog.error("Producer Start Received An Exception e:", e);
        }
    }

    public void sendMes(List<PreProductWarehouseStock> sourceDataList) throws Exception {
        for (PreProductWarehouseStock preProductWarehouseStock : sourceDataList) {
            long productId = preProductWarehouseStock.getProductId();
            int warehouseId = preProductWarehouseStock.getWarehouseId();
            ObjectMapper mapper = new ObjectMapper();
            String jsonMes = mapper.writeValueAsString(new PreStartMes(productId, warehouseId));//类转json
            try {
                Message message = new Message(topic, tag, (jsonMes).getBytes(RemotingHelper.DEFAULT_CHARSET));
                String key = String.valueOf(productId) + "_" + String.valueOf(warehouseId);//消息的key设置为品+仓
                message.setKeys(key);
                SendResult sendResult = null;
                for (int tryTime = 0; tryTime < 3; tryTime++) { //默认不成功时发三次
                    sendResult = producer.send(message);
                    if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                        mqlog.info("成功发送 mesBody:{},sendResult:{}", jsonMes, sendResult);
                        log.info("成功发送 mesBody:{},sendResult:{}", jsonMes, sendResult);
                        break;
                    } else {
                        log.info("第{}次发送消息失败，重试中...mesBody:{}", tryTime + 1, jsonMes);
                    }
                }
                if (!sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                    log.info("{}次发送消息全部失败,mesBody:{}", 3, jsonMes);
                    errorlog.error("Send Message Failed,mesBody:{},sendResult:{}", jsonMes, sendResult);
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
