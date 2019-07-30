package Logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Harsha Siriwardhana on 7/30/2019.
 */
public class Logger {

    private static final java.util.logging.Logger LOGGER;

    static {
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        setup();
    }

    public static java.util.logging.Logger getLogger() {
        return LOGGER;
    }

    private static void setup() {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);

        //console handler
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        LOGGER.addHandler(ch);

        //file handler
        // check if logs dir exists
        File logDir = new File("./logs/");
        if( !(logDir.exists()) ){
            logDir.mkdir();
        }
        FileHandler fh = null;
        try {
            fh = new FileHandler("logs/email-logger.log", true);
            fh.setLevel(Level.SEVERE);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }


    }


}
