import Utils.Constants;
import models.SendEmailAckMessage;
import models.SendEmailMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Harsha Siriwardhana on 7/29/2019.
 */
public class SMTPServerConnectionTest {

    private SMTPServerConnection connection;

    @Before
    public void setUp() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "25");
        connection = new SMTPServerConnection(props);
    }

    /*
    When smtp server is up and running
     */
    @Test
    public void sendEmailAndGetAcknowledgementSuccess() throws Exception {

        SendEmailMessage emailMessage = new SendEmailMessage();
        emailMessage.setRequestId("12");
        emailMessage.setReciepientAddress("reciever@test.com");
        emailMessage.setSenderName("sendername@test.com");
        emailMessage.setSubject("Test");
        emailMessage.setMessage("Test Message");

        SendEmailAckMessage ackMessage = connection.sendEmailAndGetAcknowledgement(emailMessage);

        Assert.assertNotNull(ackMessage);
        Assert.assertEquals(Constants.OK, ackMessage.getStatus());
        Assert.assertEquals("12", ackMessage.getRequestId());
    }

    /*
    When smtp server is down
     */
    @Test
    public void sendEmailAndGetAcknowledgementFail() throws Exception {

        SendEmailMessage emailMessage = new SendEmailMessage();
        emailMessage.setRequestId("12");
        emailMessage.setReciepientAddress("reciever@test.com");
        emailMessage.setSenderName("sendername@test.com");
        emailMessage.setSubject("Test");
        emailMessage.setMessage("Test Message");

        SendEmailAckMessage ackMessage = connection.sendEmailAndGetAcknowledgement(emailMessage);

        Assert.assertNotNull(ackMessage);
        Assert.assertEquals(Constants.ERROR, ackMessage.getStatus());
        Assert.assertEquals("12", ackMessage.getRequestId());
    }

}