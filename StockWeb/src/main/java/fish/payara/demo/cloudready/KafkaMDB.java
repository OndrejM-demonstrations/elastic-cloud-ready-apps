package fish.payara.demo.cloudready;

import fish.payara.cloud.connectors.kafka.api.KafkaListener;
import fish.payara.cloud.connectors.kafka.api.OnRecord;
import fish.payara.micro.cdi.Inbound;
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
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "testClient"),
    @ActivationConfigProperty(propertyName = "groupIdConfig", propertyValue = "test-consumer-group"),
    @ActivationConfigProperty(propertyName = "topics", propertyValue = "testing"),
    @ActivationConfigProperty(propertyName = "bootstrapServersConfig", propertyValue = "192.168.29.139:9092"),   
    @ActivationConfigProperty(propertyName = "autoCommitInterval", propertyValue = "100"),   
    @ActivationConfigProperty(propertyName = "retryBackoff", propertyValue = "1000"),   
    @ActivationConfigProperty(propertyName = "keyDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),   
    @ActivationConfigProperty(propertyName = "valueDeserializer", propertyValue = "org.apache.kafka.common.serialization.StringDeserializer"),   
    @ActivationConfigProperty(propertyName = "pollInterval", propertyValue = "1000"),   
})
public class KafkaMDB implements KafkaListener {
    
    @Inject
    @Inbound
    private Event<Stock> stockEvent;
    
    @OnRecord( topics={"testing"})
    public void getMessageTest(ConsumerRecord record) {
        System.out.println("Got record on topic testing " + record);
        Stock stock = new Stock((String)record.key(), "stock", Integer.valueOf((String)record.value()));
        stockEvent.fire(stock);
    }
}

