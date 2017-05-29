/** **************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *************************************************** */
package mihalyi.scaling.payara.inbound;

import fish.payara.cdi.jsr107.impl.NamedCache;
import fish.payara.micro.cdi.Inbound;
import io.microprofile.showcase.schedule.model.Schedule;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author Ondrej Mihalyi
 */
@ApplicationScoped
public class InboundCommandsReceiver {

    @Inject
    @NamedCache(cacheName = "cachedSchedules")
    Cache<String, Schedule> cachedSchedules;

    public void clearCache(@Observes @Inbound(eventName = "clear-cache") Object event) {
        Logger.getGlobal().info("Clearing cache...");
        cachedSchedules.removeAll();
    }
}
