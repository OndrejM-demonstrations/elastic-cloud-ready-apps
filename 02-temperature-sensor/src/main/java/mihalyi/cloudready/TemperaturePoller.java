package mihalyi.cloudready;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author omihalyi
 */
@Stateless
public class TemperaturePoller {

    @Inject
    private TemperatureSensor sensor;

    @Inject
    @ConfigProperty(name = "temperature.collector.url")
    private URL collectorUrl;

    private static Logger logger = Logger.getLogger(TemperaturePoller.class.getName());

    @Schedule(hour = "*", minute = "*", second = "*", persistent = false)
    public void sendSensorDataToColletor() throws URISyntaxException {
        int temperature = sensor.getCurrentTemperature();
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(collectorUrl.toURI()).path("temperature");
        try {
            target.request().post(Entity.entity(
                    Json.createObjectBuilder()
                            .add("timestamp", String.valueOf(System.currentTimeMillis()))
                            .add("temperature", temperature)
                            .build(),
                    MediaType.APPLICATION_JSON_TYPE));
            logger.info("Sent current temperature to a collector. Temperature: " + temperature);
        } catch (ProcessingException e) {
            logger.warning("Collector service isn't accessible, error: " + e.getMessage());
        }
    }

}
