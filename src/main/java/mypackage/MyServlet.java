package mypackage;

import java.io.PrintWriter;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MyServlet() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
    {
    	Connection connection = null;
    	try {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html><html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\" />");
            
            out.println("<title>" + "Service Bus JMS Example" + "</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<h1>" + "Service Bus JMS Example" + "</h1>");

            // Gets the JNDI context
            Context jndiContext = new InitialContext();
            // Looks up the administered objects
            
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("java:/comp/env/MyConnectionFactory");
//            ServiceBusJmsConnectionFactory connectionFactory = (ServiceBusJmsConnectionFactory) jndiContext.lookup("java:/comp/env/MyConnectionFactory");
            out.println("<p>Got the ConnectionFactory</p>");
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession();
            
            Queue queue = (Queue) jndiContext.lookup("java:/comp/env/myqueue");
            out.println("<p>Got the Queue</p>");
            MessageProducer producer = session.createProducer(queue);
            MessageConsumer consumer = session.createConsumer(queue);
            
            TextMessage text = session.createTextMessage("Test Message");
            producer.send(text);
            out.println("<p>Sent the message: " + text.getText() + "</p>");
            
            TextMessage received = (TextMessage) consumer.receive(2000);
            out.println("<p>Received the message: " + received.getText() + "</p>");
            
            out.println("</body>");
            out.println("</html>");
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
}
