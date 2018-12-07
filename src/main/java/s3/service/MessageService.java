package s3.service;

/**
 * Created by root on 31/10/18.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class MessageService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${sqs.queue.name}")
    private String queueName;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public void sendMessage(final String message) {

        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

}
