package com.mmr.rabbbitmq.tx;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通过事务的方式确认消息有没有到达
 */
public class TxSend {
    private static final String QUEUE_NAME = "test_queue_tx";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msgString = "hello tx message";
        try {
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, msgString.getBytes());

            int x = 1/0;

            System.out.println("send " + msgString);
            channel.txCommit();
        } catch (Exception e) {
            channel.txRollback();
            System.out.println("send message txRollback!");
        }
        channel.close();
        connection.close();
    }
}
