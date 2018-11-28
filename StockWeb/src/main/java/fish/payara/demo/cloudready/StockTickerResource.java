package fish.payara.demo.cloudready;

import fish.payara.micro.cdi.Inbound;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

/**
 * @author Ondro Mihalyi
 */

@Path("stock")
@ApplicationScoped
public class StockTickerResource {

    @Context
    private Sse sse;

    private volatile SseBroadcaster sseBroadcaster;

    @PostConstruct
    public void init(){
        this.sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void eventOutput(@Context SseEventSink eventSink){
        // registers the requester as a consumer of events
        sseBroadcaster.register(eventSink);
    }

    @Counted(monotonic = true, name = "incomingMessages")
    @Timed(name = "timeSpentSendingStock")
    public void broadcastToClients (
            @Observes
            @Inbound 
//            @Kafka
            Stock stock){
        sseBroadcaster.broadcast(sse.newEvent(stock.toString()));
    }

}
