import Logger.Logger;
import models.SendEmailAckMessage;
import models.SendEmailMessage;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class EmailClient {

    private static final String TAG = "EmailClient";

    private static final String SOCKET_ADDRESS = "127.0.0.1";
    private static final String SOCKET_ADDRESS_PROPERTY_NAME = "SOCKET_ADDRESS";
    private static final String DEFAULT_PORT = "9990";
    private static final String PORT_PROPRETY_NAME = "PORT";
    private static final String DEFAULT_MAIL_SENDER = "sender@gmail.com";
    private static final String MAIL_SENDER_PROPRETY_NAME = "MAIL_SENDER";
    private static final String DEFAULT_MAIL_RECIEVER = "reciever@gmail.com";
    private static final String MAIL_RECEIVER_PROPRETY_NAME = "MAIL_RECEIVER";
    private static final String DEFAULT_THREAD_COUNT = "5";
    private static final String THREAD_COUNT_PROPRETY_NAME = "THREAD_COUNT";
    private static final String DEFAULT_TOTAL_EMAIL_COUNT = "20";
    private static final String TOTAL_REQUESTS_COUNT_PROPRETY_NAME = "TOTAL_REQUESTS";

    private ExecutorService threadPool = null;
    private int threadCount = 5;
    private int emailCount = 20;
    private String senderEmail;
    private String recieverEmail;
    private int port;
    private String address;

    public EmailClient() {
        //load jvm properties at runtime
        threadCount = Integer.parseInt(System.getProperty(THREAD_COUNT_PROPRETY_NAME, DEFAULT_THREAD_COUNT));
        emailCount = Integer.parseInt(System.getProperty(TOTAL_REQUESTS_COUNT_PROPRETY_NAME, DEFAULT_TOTAL_EMAIL_COUNT));
        senderEmail = System.getProperty(MAIL_SENDER_PROPRETY_NAME, DEFAULT_MAIL_SENDER);
        recieverEmail = System.getProperty(MAIL_RECEIVER_PROPRETY_NAME, DEFAULT_MAIL_RECIEVER);
        port = Integer.parseInt(System.getProperty(PORT_PROPRETY_NAME, DEFAULT_PORT));
        address = System.getProperty(SOCKET_ADDRESS_PROPERTY_NAME, SOCKET_ADDRESS);

        getThreadPoolInstance();
    }

    public ExecutorService getThreadPoolInstance() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(threadCount);
            Logger.getLogger().info(":Thread pool created of size=" + threadCount);
            return threadPool;
        }
        return threadPool;
    }

    private void startSendingEmail() {
        //generate emails as email count value
        List<SendEmailMessage> emailList = IntStream.range(0, emailCount)
                .mapToObj(this::generateRandomEmail)
                .collect(Collectors.toList());

        //completableFuture with custom executor
        List<CompletableFuture<SendEmailAckMessage>> futureList = emailList.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> sendMessagesToServer(t), getThreadPoolInstance()))
                .collect(Collectors.toList());

        futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        //shutting down thread pool
        getThreadPoolInstance().shutdown();
    }

    public SendEmailAckMessage sendMessagesToServer(SendEmailMessage sendEmailMessage) {

        try (Socket socket = new Socket(address, port);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ) {

            Logger.getLogger().info(": client sending [ mail id = " + sendEmailMessage.getRequestId() + "] Running on theread -> " + Thread.currentThread().getName());
            outputStream.writeObject(sendEmailMessage);

            SendEmailAckMessage ack = (SendEmailAckMessage) inputStream.readObject();
            Logger.getLogger().info(": client received acknowledgement - mail id = " + ack.getRequestId());
            return ack;

        } catch (Exception e) {
            Logger.getLogger().severe("Error occured:" + e);
        }


        return null;
    }

    /*
    Generate random SendEmailMessage object
     */
    private SendEmailMessage generateRandomEmail() {
        SendEmailMessage email = new SendEmailMessage();

        //generate random UUID value as request id
        email.setRequestId(UUID.randomUUID().toString());
        email.setSenderName(senderEmail);
        email.setReciepientAddress(recieverEmail);
        email.setSubject(email.getRequestId());
        email.setMessage(" Req.Id =" + email.getRequestId());

        return email;
    }

    /*
    Generate random SendEmailMessage object
     */
    private SendEmailMessage generateRandomEmail(int requestId) {
        SendEmailMessage email = new SendEmailMessage();

        //generate random UUID value as request id
        email.setRequestId(Integer.toString(requestId));
        email.setSenderName(senderEmail);
        email.setReciepientAddress(recieverEmail);
        email.setSubject(email.getRequestId());
        email.setMessage(" Req.Id =" + email.getRequestId());

        return email;
    }

    public static void main(String[] args) {

        EmailClient emailClient = new EmailClient();
        System.out.println(TAG + "client starting ...");
        emailClient.startSendingEmail();
    }
}
