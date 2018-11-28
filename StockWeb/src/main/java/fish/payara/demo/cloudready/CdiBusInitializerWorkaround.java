package fish.payara.demo.cloudready;

import fish.payara.micro.cdi.ClusteredCDIEventBus;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Workaround for a bug in Payara Micro 5 Alpha 4, due to which initial CDI event isn't triggered and CDI bus isn't initialized
 * 
 * @author Ondrej Mihalyi
 */
@Singleton
@Startup
public class CdiBusInitializerWorkaround {
    
    @Inject
    ClusteredCDIEventBus eventBus;
    
    @PostConstruct
    public void startup() {
        eventBus.initialize();
    }
}

