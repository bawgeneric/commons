package io.kodokojo.commons.bdd.feature;

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

import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.junit.ScenarioTest;
import io.kodokojo.commons.DockerIsRequire;
import io.kodokojo.commons.DockerPresentMethodRule;
import io.kodokojo.commons.bdd.Consul;
import io.kodokojo.commons.bdd.stage.docker.consul.PropertyValueConsulGiven;
import io.kodokojo.commons.bdd.stage.docker.consul.PropertyValueConsulThen;
import io.kodokojo.commons.bdd.stage.docker.consul.PropertyValueConsulWhen;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;


@As("Consul PropertyValueProvider")
@Consul
public class ConsulPropertyValueProviderIntTest extends ScenarioTest<PropertyValueConsulGiven<?>, PropertyValueConsulWhen<?>, PropertyValueConsulThen<?>> {

    @Rule
    public DockerPresentMethodRule dockerPresentMethodRule = new DockerPresentMethodRule();

    @Test
    @DockerIsRequire
    public void retrieve_valid_kv() {

        given().consul_is_started()
                .and().consul_kv_value_provider_exist()
                .and().add_key_$_with_value_$_to_consul("maCle", "maValeur");
        when().request_to_provide_value_for_key_$("maCle");
        then().it_exist_a_key_$_with_value_$("maCle", "maValeur");
    }

}
