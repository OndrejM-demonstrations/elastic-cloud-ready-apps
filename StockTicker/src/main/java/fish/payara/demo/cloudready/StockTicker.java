package fish.payara.demo.cloudready;

import fish.payara.micro.cdi.Outbound;
import javax.ejb.Schedule;
import javax.enterprise.event.Event;
import javax.inject.Inject;


import java.io.Serializable;
import javax.ejb.Stateless;
import javax.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@Stateless
@Counted(monotonic = true, name = "outgoingMessages")
@Timed(name = "timeSpentDispatchingStock")
public class StockTicker implements Serializable {

    @Inject
    @Outbound
//    @Kafka
    private Event<Stock> stockEvents;

    @ConfigProperty(name = "stockticker.symbol", defaultValue = "Dow Jones")
    @Inject
    private Provider<String> symbolConfig;

    @Schedule(hour = "*", minute = "*", second = "*/1", persistent = true)
    public void generatePrice() {
        Stock stock = new Stock(symbolConfig.get(), "Conference stock", Math.random() * 100.0);
        stockEvents.fire(stock);
    }

}
