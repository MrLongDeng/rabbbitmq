package com.mmr.rabbbitmq.simple;

import com.mmr.rabbbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者获取消息
 */
public class Recv {
    private static final String Queue_Name = "test_simple_queue";
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(Queue_Name, false, false, false, null);
        //定义列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //获取到达消息
            @Override
                public void handleDelivery (String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                byte[] body) throws IOException {
                    String msgString = new String(body, "utf-8");
                    System.out.println("new api recv: " + msgString);
                }
            };
            //监听队列
        channel.basicConsume(Queue_Name,true, consumer);
        }
    }

