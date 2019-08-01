package com.mmr.rabbbitmq.topic;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    private static final String QUEUE_NAME = "test_queue_topic_2";
    private static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "goods.#");

        channel.basicQos(1); //保证一次只分发一个

        //定义一个消费者
        Consumer consumer = new DefaultConsumer(channel){
            //消息到达后触发方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println("[2] Recv msg: " + msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[2] done ");

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false; //自动应答
        //监听队列
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}