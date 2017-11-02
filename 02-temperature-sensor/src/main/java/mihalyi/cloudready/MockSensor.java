package mihalyi.cloudready;

import java.util.Random;
import javax.enterprise.context.Dependent;

/**
 *
 * @author omihalyi
 */
@Dependent
public class MockSensor implements TemperatureSensor {

    private Random random = new Random();
    
    @Override
    public int getCurrentTemperature() {
        return random.nextInt(50);
    }
    
}
