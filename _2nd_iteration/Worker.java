import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker {
    public final static String QUEUE_NAME="task";
    static JLabel[] taskLabels;
    static JTextArea[] areas;
    public static void main(String[] args) throws IOException, TimeoutException {
	int nbTasks = Integer.parseInt(args[0]);
        receive(nbTasks);
    }

    static void receive(int nbTasks) throws IOException, TimeoutException {
        JFrame f= new JFrame("Worker");
	taskLabels = new JLabel[nbTasks];
	areas = new JTextArea[nbTasks];
	
	for(int i=1; i<=nbTasks; i++) {
		taskLabels[i-1]=new JLabel("Part "+i+" : ");
        	taskLabels[i-1].setBounds(10,30+150*(i-1), 300,30);
        	f.add(taskLabels[i-1]);

		areas[i-1] = new JTextArea();
        	areas[i-1].setBounds(20,50+150*(i-1), 300,100);
		f.add(areas[i-1]);
	}
        f.setSize(500,500);
        f.setLayout(null);
        f.setVisible(true);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
	Channel[] channels = new Channel[nbTasks];
	for(int i=1; i<=nbTasks; i++) {
        	channels[i-1] = connection.createChannel();
        	channels[i-1].queueDeclare(QUEUE_NAME+i,false,false,false,null);
	}

	for(int i=1; i<nbTasks+1; i++) {
		final int num = i-1;
        	DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            		String receivedMessage = new String(delivery.getBody(),"UTF-8");
            		areas[num].setText(receivedMessage);

        	};
       		channels[i-1].basicConsume(QUEUE_NAME+i,true,deliverCallback,consumerTag -> {});
	}
    }
}
