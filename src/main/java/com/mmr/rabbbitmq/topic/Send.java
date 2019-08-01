package com.mmr.rabbbitmq.topic;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String msgString = "商品。。。";
        channel.basicPublish(EXCHANGE_NAME, "goods.delete", null, msgString.getBytes());
        System.out.println("---send " + msgString);
        channel.close();
        connection.close();
    }
}
