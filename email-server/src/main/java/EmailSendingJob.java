import models.SendEmailAckMessage;
import models.SendEmailMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class EmailSendingJob implements Runnable {

    private Socket socket;
    private SMTPServerConnection serverConnection;
    private static final String TAG = "EmailSendingJob";

    public EmailSendingJob(Socket socket, SMTPServerConnection serverConnection) {
        this.socket = socket;
        this.serverConnection = serverConnection;
    }

    @Override
    public void run() {

        try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ) {
            SendEmailMessage email = (SendEmailMessage) inputStream.readObject();
            System.out.println(TAG + ":sending email -> " + email.toString());


            SendEmailAckMessage ackMessage = serverConnection.sendEmailAndGetAcknowledgement(email);
            outputStream.writeObject(ackMessage);
            System.out.println(TAG+ ":Ack message-> " + ackMessage.getRequestId());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + "Error while processing email sending.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(TAG + ":Exception while socket closing ->" + e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
