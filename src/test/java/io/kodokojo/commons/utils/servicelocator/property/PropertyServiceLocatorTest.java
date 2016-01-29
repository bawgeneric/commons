package io.kodokojo.commons.utils.servicelocator.property;

/*
 * #%L
 * docker-commons
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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