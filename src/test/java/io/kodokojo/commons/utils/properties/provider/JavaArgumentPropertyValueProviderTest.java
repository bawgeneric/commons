package io.kodokojo.commons.utils.properties.provider;

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

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaArgumentPropertyValueProviderTest {

    @Test
    public void simple_args() {

        JavaArgumentPropertyValueProvider valueProvider = new JavaArgumentPropertyValueProvider(new String[]{"--maCle", "value"});

        String value = valueProvider.provideValue("maCle");

        assertThat(value).isEqualTo("value");
    }

    @Test
    public void arguments_has_no_key_defined_args() {

        JavaArgumentPropertyValueProvider valueProvider = new JavaArgumentPropertyValueProvider(new String[]{"--maCle", "value", "truc"});

        assertThat(valueProvider.values()).doesNotContain("truc").contains("value");
    }

    @Test
    public void provider_with_custom_prefixe() {

        JavaArgumentPropertyValueProvider valueProvider = new JavaArgumentPropertyValueProvider("@-@",new String[]{"@-@maCle", "value", "truc"});

        String value = valueProvider.provideValue("maCle");

        assertThat(value).isEqualTo("value");
        assertThat(valueProvider.values()).doesNotContain("truc").contains("value");
    }

    @Test
    public void argument_without_value() {

        JavaArgumentPropertyValueProvider valueProvider = new JavaArgumentPropertyValueProvider(new String[]{"--maCle1" ,"--maCle", "value", "--maCle3"});

        String value = valueProvider.provideValue("maCle");
        assertThat(value).isEqualTo("value");

        Boolean res = valueProvider.providePropertyValue(Boolean.class, "maCle1");
        assertThat(res).isTrue();
    }

}