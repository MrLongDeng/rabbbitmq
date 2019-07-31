package com.mmr.rabbbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 连接rabbitmq
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置服务地址
        connectionFactory.setHost("127.0.0.1");
        //设置端口号 AMQP协议
        connectionFactory.setPort(5672);
        //连接哪个virtual host（相当于连接哪个mysql数据库）
        connectionFactory.setVirtualHost("/vhost_mmr");
        //设置用户名
        connectionFactory.setUsername("user_mmr");
        //设置密码
        connectionFactory.setPassword("123");
        return connectionFactory.newConnection();
    }
}
