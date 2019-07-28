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
    private ServerSocket serverSocket = null;
    private SMTPServerConnection serverConnection = null;

    private int threadCount = 20;
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
        //properties for mail server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", false);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "127.0.0.1");
        properties.put("mail.smtp.port", "25");

        //create a thread pool if not exists
        getThreadPoolInstance();
        serverConnection = new SMTPServerConnection(properties);
        serverInitiated = true;
    }

    private void startServer() {
        try (ServerSocket socketListner = new ServerSocket(defaultPort)) {
            System.out.println(TAG + ":The Email Server is running...");
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
