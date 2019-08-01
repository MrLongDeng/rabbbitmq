package com.mmr.rabbbitmq.confirm;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * confirm普通模式
 */
public class send3 {
    private static final String QUEUE_NAME = "test_queue_confirm3";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //生产者调用confirmSelect将channel设置为confirm模式，注意：同一个队列不能既设为事务确认，又设为
        //confirm确认，不然会报错
        channel.confirmSelect();

        //未确认的消息标识
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        //添加监听通道
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple){
                    System.out.println("----handleNack-----multiple");
                    confirmSet.headSet(deliveryTag+1).clear();
                }else {
                    System.out.println("----handleNack----multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }

            //没有问题的handleAck
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple){
                    System.out.println("----handleAck----multiple");
                    confirmSet.headSet(deliveryTag+1).clear();
                }else {
                    System.out.println("----handAck------multiple false");
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String msgString = "hello confirm message";

        while (true){
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, msgString.getBytes());
            confirmSet.add(seqNo);
        }

    }
}
