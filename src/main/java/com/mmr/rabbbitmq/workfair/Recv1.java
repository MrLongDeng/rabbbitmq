package com.mmr.rabbbitmq.workfair;


import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    private static final String QUEUE_NAME = "test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //获取通道
        final Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1); //保证一次只分发一个

        //定义一个消费者
        Consumer consumer = new DefaultConsumer(channel){
           //消息到达后触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              String msg = new String(body, "utf-8");
              System.out.println("[1] Recv msg: " + msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[1] done ");

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false; //自动应答
        //监听队列
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
