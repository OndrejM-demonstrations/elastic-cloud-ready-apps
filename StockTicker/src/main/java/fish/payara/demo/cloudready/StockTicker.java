package fish.payara.demo.cloudready;

import javax.ejb.Schedule;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import fish.payara.micro.cdi.Outbound;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Mike Croft
 */
@Stateless
public class StockTicker implements Serializable {

    @Inject
    @Outbound(loopBack = true)
    private Event<Stock> stockEvents;

    @ConfigProperty(name = "stockticker.symbol", defaultValue = "Dow Jones")
    @Inject
    private Provider<String> symbolConfig;

    @Schedule(hour = "*", minute = "*", second = "*/1", persistent = true)
    private void generatePrice() {
        Stock stock = new Stock(symbolConfig.get(), "Conference stock", Math.random() * 100.0);
        stockEvents.fire(stock);
    }

}
