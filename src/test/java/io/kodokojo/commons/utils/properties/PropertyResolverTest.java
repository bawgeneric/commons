package io.kodokojo.commons.utils.properties;

/*
 * #%L
 * kodokojo-commons
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

import io.kodokojo.commons.utils.properties.provider.PropertyValueProvider;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class PropertyResolverTest {

    private PropertyResolver resolver = new PropertyResolver(new PropertyValueProvider() {
        @Override
        public <T> T providePropertyValue(Class<T> classType, String key) {
            switch (key) {
                case "maCle":
                    return (T) "MaValue";
                case "maCleWithDefault":
                    return (T) "MaSecondeValue";
            }
            return null;
        }
    });

    @Test
    public void valid_simple_config() {
        SimpleConfig simpleConfig = resolver.createProxy(SimpleConfig.class);
        String value = simpleConfig.value();
        assertThat(value).isEqualTo("MaValue");
    }

    @Test
    public void valid_with_deault_value_with_data_filled() {
        WithDefaultValueConfig config = resolver.createProxy(WithDefaultValueConfig.class);
        String value = config.valueWithDefault();
        assertThat(value).isEqualTo("MaSecondeValue");
    }

    @Test
    public void valid_which_return_default_value() {
        WithDefaultValueConfig config = resolver.createProxy(WithDefaultValueConfig.class);
        String value = config.valueFilledWithDefault();
        assertThat(value).isEqualTo("maDefaultValue");
    }

    @Test
    public void check_default_value_type_is_valid() {
        DefaultValue config = resolver.createProxy(DefaultValue.class);

        String stringValue = config.stringValue();
        assertThat(stringValue).isEqualTo("string");

        int intValue = config.intValue();
        assertThat(intValue).isEqualTo(42);

        long longValue = config.longValue();
        assertThat(longValue).isEqualTo(300L);

        BigDecimal bigDecimal = config.bigDecimalValue();
        assertThat(bigDecimal).isEqualTo(new BigDecimal("1234567890.005"));

        double doubleValue = config.doubleValue();
        assertThat(doubleValue).isBetween(1.00, 1.02);

        assertThat(config.booleanValue()).isTrue();
    }

    interface SimpleConfig extends PropertyConfig {
        @Key("maCle")
        String value();
    }

    interface WithDefaultValueConfig extends PropertyConfig {
        @Key(value = "maCleWithDefault", defaultValue = "maDefaultValue")
        String valueWithDefault();

        @Key(value = "maCleFilledWithDefault", defaultValue = "maDefaultValue")
        String valueFilledWithDefault();
    }

    interface DefaultValue extends PropertyConfig {

        @Key(value = "string", defaultValue = "string")
        String stringValue();

        @Key(value = "int", defaultValue = "42")
        int intValue();

        @Key(value = "long", defaultValue = "300")
        long longValue();

        @Key(value = "bigdecimal", defaultValue = "1234567890.005")
        BigDecimal bigDecimalValue();

        @Key(value = "double", defaultValue = "1.01")
        double doubleValue();

        @Key(value = "boolean", defaultValue = "true")
        boolean booleanValue();

    }


}