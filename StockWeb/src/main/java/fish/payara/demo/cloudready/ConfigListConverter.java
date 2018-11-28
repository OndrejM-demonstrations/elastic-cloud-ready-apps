package fish.payara.demo.cloudready;

import org.eclipse.microprofile.config.spi.Converter;

/**
 *
 * @author Ondrej Mihalyi
 */
public class ConfigListConverter implements Converter<String[]> {

    @Override
    public String[] convert(String value) {
        return value.split(";");
    }

}

