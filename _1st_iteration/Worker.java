import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker {
    public final static String QUEUE_NAME1="task1";
    public final static String QUEUE_NAME2="task2";
    static JLabel taskLabel1, taskLabel2;
    static JTextArea area1, area2;
    public static void main(String[] args) throws IOException, TimeoutException {
        receive();
    }

    static void receive() throws IOException, TimeoutException {
        JFrame f= new JFrame("Task3");

        taskLabel1=new JLabel("Part 1 : ");
        taskLabel1.setBounds(10,30, 300,30);
        f.add(taskLabel1);

	area1 = new JTextArea();
        area1.setBounds(20,80, 300,100);
	f.add(area1);


        taskLabel2=new JLabel("Part 2 : ");
        taskLabel2.setBounds(10,200, 300,30);
        f.add(taskLabel2);

	area2 = new JTextArea();
        area2.setBounds(20,250, 300,100);
	f.add(area2);

        f.setSize(500,500);
        f.setLayout(null);
        f.setVisible(true);


        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME1,false,false,false,null);


        Connection connection2 = connectionFactory.newConnection();
        Channel channel2 = connection2.createChannel();
        channel2.queueDeclare(QUEUE_NAME2,false,false,false,null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody(),"UTF-8");
            area1.setText(receivedMessage);

        };
        channel.basicConsume(QUEUE_NAME1,true,deliverCallback,consumerTag -> {});

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String receivedMessage = new String(delivery.getBody(),"UTF-8");
            area2.setText(receivedMessage);

        };
        channel.basicConsume(QUEUE_NAME2,true,deliverCallback2,consumerTag -> {});
    }
}
