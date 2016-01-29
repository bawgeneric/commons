package io.kodokojo.commons.utils.servicelocator.property;

import io.kodokojo.commons.utils.properties.provider.JavaArgumentPropertyValueProvider;
import io.kodokojo.commons.utils.properties.provider.PropertyValueProvider;
import io.kodokojo.commons.utils.servicelocator.Service;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyServiceLocatorTest {


    @Test
    public void simple_locate_service_by_name() {
        PropertyValueProvider propertyValueProvider = new JavaArgumentPropertyValueProvider(new String[]{"--registry.host", "localhost", "--registry.port", "5000"});
        PropertyServiceLocator serviceLocator = new PropertyServiceLocator(propertyValueProvider);

        Set<Service> services = serviceLocator.getServiceByName("registry");

        System.out.println(services);

        assertThat(services).isNotEmpty();

    }

}