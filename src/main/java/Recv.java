



import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        // 设置服务端的地址、端口、用户名和密码...
        factory.setHost("localhost");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("testhost");
        factory.setUsername("admin");
        factory.setPassword("admin");

        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare("q_test_01", false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body);
                System.out.println("Received: " + message);
                // 消息确认
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // 关闭自动消息确认，autoAck = false
        channel.basicConsume("q_test_01", true, consumer);
    }

}
