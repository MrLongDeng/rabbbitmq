package com.mmr.rabbbitmq.confirm;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * confirm普通模式
 */
public class send2 {
    private static final String QUEUE_NAME = "test_queue_confirm1";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //生产者调用confirmSelect将channel设置为confirm模式，注意：同一个队列不能既设为事务确认，又设为
        //confirm确认，不然会报错
        channel.confirmSelect();
        String msgString = "hello confirm message";
        //批量发送
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", QUEUE_NAME, null, msgString.getBytes());
        }
        //确认
        if (!channel.waitForConfirms()){
            System.out.println("message send failed");
        }else{
            System.out.println("message send ok");
        }
        channel.close();
        connection.close();

    }
}
