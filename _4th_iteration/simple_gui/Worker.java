import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Worker {
    public final static String QUEUE_NAME="task";
    static JTextArea area;

    public static void main(String[] args) throws IOException, TimeoutException {
	int numTaskCourant = Integer.parseInt(args[0]);
	int nbTasks = Integer.parseInt(args[1]);
        receive(numTaskCourant,nbTasks);
    }

    static void receive(int numTaskCourant,int nbTasks) throws IOException, TimeoutException {
        JFrame f= new JFrame("Worker"+numTaskCourant);

	JLabel wrtnPolicy = new JLabel(), infoWrtn = new JLabel();
	wrtnPolicy.setForeground(java.awt.Color.RED); 
	infoWrtn.setForeground(java.awt.Color.RED);
	wrtnPolicy.setText("Seulement le texte ecrit dans votre partie (Part "+numTaskCourant+") sera pris en consideration !");
	infoWrtn.setText("Toute modification dans les autres parties NE CHANGERA RIEN <3");
        wrtnPolicy.setBounds(10,0, 700,20);
	infoWrtn.setBounds(10,15, 700,20);
	f.add(wrtnPolicy); f.add(infoWrtn);

	String placeholder = "";
	for(int i=1; i<=nbTasks; i++) {
		placeholder+="Part "+i+" :\n";
	}

	area = new JTextArea(placeholder);
        area.setBounds(20,50, 300,100*nbTasks);
	f.add(area);

	JButton button = new JButton("Send");
	button.setBounds(350,200,100,30);
        button.addActionListener(new ActionListener() {
        	@Override
       		public void actionPerformed(ActionEvent e) {
			String content = area.getText(), contentToSend = "";
			int beginIndex = content.indexOf("Part "+numTaskCourant)+8, endIndex = content.indexOf("Part "+(numTaskCourant+1));
			if (endIndex == -1) { //C'est la dernière partie dans l'edition
				endIndex = content.length()-1;
			}
			contentToSend = content.substring(beginIndex,endIndex);

			for(int i=1; i<=nbTasks; i++) {
				if(i!=numTaskCourant) {
                			Send sender = new Send(contentToSend,"task"+i+""+numTaskCourant);
                			sender.sendMessage();
				}
			}
		}
        });
        f.add(button);
	
        f.setSize(500,500*nbTasks);
        f.setLayout(null);
        f.setVisible(true);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
	Channel[] channels = new Channel[nbTasks];
	for(int i=1; i<=nbTasks; i++) {
	    if( i!=numTaskCourant) {
        	channels[i-1] = connection.createChannel();
        	channels[i-1].queueDeclare(QUEUE_NAME+numTaskCourant+""+i,false,false,false,null);
	    }
	}

	for(int i=1; i<nbTasks+1; i++) {
	    final int num = i;
	    if( i!=numTaskCourant) {
        	DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String preText = area.getText().substring(0,area.getText().indexOf("Part "+num)+8);
			String postText = "";
			if( num!=nbTasks ) {
				postText = area.getText().substring(area.getText().indexOf("Part "+(num+1)));
			}
            		String receivedMessage = new String(delivery.getBody(),"UTF-8");
            		area.setText(preText+receivedMessage+postText);

        	};
       		channels[i-1].basicConsume(QUEUE_NAME+numTaskCourant+""+i,true,deliverCallback,consumerTag -> {});
	   }
	}
    }
}
