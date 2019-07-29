import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Harsha Siriwardhana on 7/28/2019.
 */
public class EmailServer {

    private static final String TAG = "EmailServer";
    private ExecutorService threadPool = null;
    private SMTPServerConnection serverConnection = null;

    private static final String DEFAULT_THREAD_COUNT = "5";
    private static final String THREAD_COUNT_PROPRETY_NAME = "THREADS";
    private static final String DEFAULT_PORT = "9990";
    private static final String PORT_PROPRETY_NAME = "PORT";
    private static final String SMTP_HOST_ADDRESS = "127.0.0.1";
    private static final String SMTP_HOST_ADDRESS_PROPERTY_NAME= "SMTP_HOST_ADDRESS";
    private static final String SMTP_PORT = "25";
    private static final String SMTP_PORT_PROPERTY_NAME = "SMTP_PORT";
    private int threadCount = 5;
    private int defaultPort = 9990;
    private boolean serverInitiated = false;

    public ExecutorService getThreadPoolInstance() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(threadCount);
            System.out.println(TAG + ":Thread pool created of size=" + threadCount);
            return threadPool;
        }
        return threadPool;
    }

    public EmailServer() {
        //loading jvm properties passed in runtime
        threadCount = Integer.parseInt(System.getProperty(THREAD_COUNT_PROPRETY_NAME, DEFAULT_THREAD_COUNT));
        defaultPort = Integer.parseInt(System.getProperty(PORT_PROPRETY_NAME,DEFAULT_PORT));

        String smtpAddress = System.getProperty(SMTP_HOST_ADDRESS_PROPERTY_NAME,SMTP_HOST_ADDRESS);
        String smtpPort = System.getProperty(SMTP_PORT_PROPERTY_NAME,SMTP_PORT);
        //end loading jvm properties

        //properties for mail server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", false);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpAddress);
        properties.put("mail.smtp.port", smtpPort);

        //create a thread pool if not exists
        getThreadPoolInstance();
        serverConnection = new SMTPServerConnection(properties);
        serverInitiated = true;
    }

    private void startServer() {
        try (ServerSocket socketListner = new ServerSocket(defaultPort)) {
            System.out.println(TAG + ":The Email Server is running on port = " + defaultPort);
            while (serverInitiated) {
                Socket socket = socketListner.accept();
                EmailSendingJob job = new EmailSendingJob(socket,serverConnection);
                getThreadPoolInstance().execute(job);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG + ":Email server error while connecting to the socket");
        }
    }

    public static void main(String[] args) {

        EmailServer emailServer = new EmailServer();
        System.out.println(TAG + ":Server starting ...");
        emailServer.startServer();
    }
}
