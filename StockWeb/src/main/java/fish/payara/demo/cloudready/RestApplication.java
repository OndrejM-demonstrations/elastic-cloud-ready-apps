package fish.payara.demo.cloudready;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Ondrej Mihalyi
 */
@ApplicationPath("rest")
@ApplicationScoped
public class RestApplication extends Application {
}

