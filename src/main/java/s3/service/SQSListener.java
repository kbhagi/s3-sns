package s3.service;

/**
 * Created by root on 30/10/18.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * Created by gkatziourasemmanouil on 25/02/16.
 */
@Component
public class SQSListener implements MessageListener {
    @Autowired
    Records r;
    private static final Logger LOGGER = LoggerFactory.getLogger(SQSListener.class);

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {

            LOGGER.info("Received message " + r.deserializeObject( textMessage.getText())+ "\n"+textMessage.getJMSMessageID());
        } catch (JMSException e) {
            LOGGER.error("Error processing message ", e);
        }
    }



}
