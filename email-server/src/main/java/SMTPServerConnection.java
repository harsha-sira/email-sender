import Utils.Constants;
import models.SendEmailAckMessage;
import models.SendEmailMessage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class SMTPServerConnection {

    private Session session;


    public SMTPServerConnection(Properties properties) {
        this.session = Session.getInstance(properties, new Authenticator() {
            // no need to override password authentication since it is not required.
        });
    }

    public SendEmailAckMessage sendEmailAndGetAcknowledgement(SendEmailMessage emailMessage) {
        SendEmailAckMessage ackMessage = new SendEmailAckMessage(emailMessage.getRequestId());

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailMessage.getSenderName()));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailMessage.getReciepientAddress()));
            message.setSubject(emailMessage.getSubject());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailMessage.getMessage(), "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
            ackMessage.setStatus(Constants.OK);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ackMessage;
    }


}


