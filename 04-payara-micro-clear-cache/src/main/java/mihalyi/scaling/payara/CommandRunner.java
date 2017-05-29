/****************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 ****************************************************/

package mihalyi.scaling.payara;

import fish.payara.micro.cdi.Outbound;
import java.util.logging.Logger;
import javax.enterprise.context.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Ondrej Mihalyi
 */
@RequestScoped
@Named
public class CommandRunner {
    
    private String message;
    
    @Inject
    @Outbound(eventName = "clear-cache")
    private Event<Object> clearCacheEvent;
    
    public void sendClearCacheCommand() {
        clearCacheEvent.fire("");
        message = "Clear cache command issued, cache should be clean now";
        Logger.getGlobal().info(message);
    }

    public String getMessage() {
        return message;
    }
    
}

