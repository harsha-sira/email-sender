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

    private ExecutorService threadPool = null;
    private int threadCount = 5;
    private int emailCount = 20;

    public EmailClient() {
        getThreadPoolInstance();
    }

    public ExecutorService getThreadPoolInstance() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(threadCount);
            System.out.println(TAG + ":Thread pool created of size=" + threadCount);
            return threadPool;
        }
        return threadPool;
    }

    private void startSendingEmail() {
        //generate emails as email count value
        List<SendEmailMessage> emailList = IntStream.range(0, emailCount)
                .mapToObj(i -> generateRandomEmail())
                .collect(Collectors.toList());

        //completableFuture with custom executor
        List<CompletableFuture<String>> futureList = emailList.stream()
                .map(t -> CompletableFuture.supplyAsync(() -> sendMessagesToServer(t), getThreadPoolInstance()))
                .collect(Collectors.toList());

        futureList.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());


        //shutting down thread pool
        getThreadPoolInstance().shutdown();
    }

    public String sendMessagesToServer(SendEmailMessage sendEmailMessage) {


        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 9998);
            System.out.println("Connected");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(sendEmailMessage);

//            System.out.println(TAG + ":ack = " + inputStream.readObject().toString() );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG+ "Error occured");
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return sendEmailMessage.getRequestId();
    }

    /*
    Generate random SendEmailMessage object
     */
    private SendEmailMessage generateRandomEmail() {
        SendEmailMessage email = new SendEmailMessage();

        //generate random UUID value as request id
        email.setRequestId(UUID.randomUUID().toString());
        email.setSenderName("Email Client");
        email.setReciepientAddress("outbox@test.com");
        email.setSubject("Email Sending");
        email.setMessage(" Sample email" + email.getRequestId());

        return email;
    }

    public static void main(String[] args) {

        EmailClient emailClient = new EmailClient();
        System.out.println(TAG + "client starting ...");
        emailClient.startSendingEmail();
    }
}
