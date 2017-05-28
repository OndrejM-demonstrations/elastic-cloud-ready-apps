package io.microprofile.showcase.bootstrap;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class BootstrapDataProducer {

    public BootstrapData load() {
        final URL resource = Thread.currentThread().getContextClassLoader().getResource("schedule.json");
        assert resource !=null : "Failed to load 'schedule.json'";

        final Parser parser = new Parser();
        final BootstrapData data = parser.parse(resource);

        Logger.getLogger(BootstrapData.class.getName()).log(Level.INFO, "Schedule contains "+data.getSessions().size() + " sessions");

        return data;
    }

}

