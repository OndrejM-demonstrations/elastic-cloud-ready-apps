package fish.payara.demo.cloudready;

import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author Ondrej Mihalyi
 */
@ConnectionFactoryDefinition(name = "java:global/env/KafkaConnectionFactory",
        description = "Kafka Connection Factory",
        interfaceName = "fish.payara.cloud.connectors.kafka.KafkaConnectionFactory",
        resourceAdapter = "kafka-rar",
        properties = {
            "bootstrapServersConfig=localhost:9092"
        })
@Stateless
public class KafkaStockPublisher {

    @Resource(lookup = "java:global/env/KafkaConnectionFactory")
    KafkaConnectionFactory factory;

    public void publishStock(@Observes @Kafka Stock stock) {
        try (KafkaConnection conn = factory.createConnection()) {
            conn.send(new ProducerRecord("stock", stock.getSymbol(), String.valueOf(stock.getPrice())));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
