/** **************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *************************************************** */
package mihalyi.scaling.payara.inbound;

import com.hazelcast.core.HazelcastInstance;
import fish.payara.micro.cdi.Inbound;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 *
 * @author Ondrej Mihalyi
 */
@ApplicationScoped
public class InboundHazelcastMapCommandsReceiver {

    @Resource(lookup = "payara/Hazelcast")
    private HazelcastInstance hazelcast;

    public void clearCache(@Observes @Inbound(eventName = "clear-cache") Object event) {
        //Logger.getGlobal().info("Clearing cache...");
        hazelcast.getMap("cachedSchedules").clear();
    }
}
