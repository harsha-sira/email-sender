import models.SendEmailAckMessage;
import models.SendEmailMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Harsha Siriwardhana on 7/30/2019.
 */
public class EmailClientTest {

    SendEmailMessage emailMessage;
    @Before
    public void setUp() throws Exception {
        emailMessage = new SendEmailMessage();
        emailMessage.setRequestId("1");
        emailMessage.setMessage("1");
        emailMessage.setSenderName("sender@test.com");
        emailMessage.setReciepientAddress("receive@test.com");
        emailMessage.setSubject("test");
    }

    @Test
    public void sendMessagesToServer() throws Exception {
        EmailClient emailClient = new EmailClient();

        SendEmailAckMessage ack = emailClient.sendMessagesToServer(emailMessage);
//        if both servers  up
        Assert.assertEquals("OK",ack.getStatus());
        Assert.assertNotNull(ack);
        Assert.assertEquals("1", ack.getRequestId());
    }
}