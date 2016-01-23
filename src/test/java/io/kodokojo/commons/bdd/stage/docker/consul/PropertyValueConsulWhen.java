package io.kodokojo.commons.bdd.stage.docker.consul;

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

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import io.kodokojo.commons.utils.properties.provider.kv.ConsulKvPropertyValueProvider;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

public class PropertyValueConsulWhen<SELF extends PropertyValueConsulWhen<?>> extends Stage<SELF> {

    @ExpectedScenarioState
    ConsulKvPropertyValueProvider consulKvPropertyValueProvider;

    @ProvidedScenarioState
    Map<String,Object> consulValueProvides = new HashMap<>();

    public SELF request_to_provide_value_for_key_$(@Quoted String key) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        consulValueProvides.put(key, consulKvPropertyValueProvider.providePropertyValue(String.class, key));
        return self();
    }

    public SELF request_to_provide_value_for_key_$_with_expected_type_$(@Quoted String key, Class expectedType) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        consulValueProvides.put(key, consulKvPropertyValueProvider.providePropertyValue( expectedType, key));
        return self();
    }

}
