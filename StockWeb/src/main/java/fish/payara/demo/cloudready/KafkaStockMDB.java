package fish.payara.demo.cloudready;

import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 *
 * @author Ondrej Mihalyi
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "topics", propertyValue = "stock")
    ,
    @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "demo")
    ,
    @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "localhost:9092")
    ,
    @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer")
    ,
    @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer")
})
public class KafkaStockMDB implements KafkaListener {

    @Inject
    @Kafka
    private Event<Stock> stockEvent;

    @OnRecord
    public void onMessage(ConsumerRecord<Object, String> record) {
        System.out.println("Got record on topic stock " + record);
        Stock stock = new Stock((String) record.key(), "stock", Double.valueOf((String) record.value()));
        stockEvent.fire(stock);
    }
}
