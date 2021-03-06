package com.mmr.rabbbitmq.ps;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * fanout交换机
 */
public class send {
    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //发送消息
        String msg = "hello ps";
        //消息往交换机里面发
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
        System.out.println("send: " + msg);

        channel.close();
        connection.close();
    }
}
