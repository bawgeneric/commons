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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.squareup.okhttp.*;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import io.kodokojo.commons.config.DockerConfig;
import io.kodokojo.commons.utils.DockerTestSupport;
import io.kodokojo.commons.utils.docker.DockerSupport;
import io.kodokojo.commons.utils.properties.PropertyResolver;
import io.kodokojo.commons.utils.properties.provider.*;
import io.kodokojo.commons.utils.properties.provider.kv.ConsulKvPropertyValueProvider;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.http.Body;
import retrofit.http.Path;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PropertyValueConsulGiven<SELF extends PropertyValueConsulGiven<?>> extends Stage<SELF> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyValueConsulGiven.class);

    @Rule
    @ProvidedScenarioState
    public DockerTestSupport dockerClientSupport = new DockerTestSupport();


    @ProvidedScenarioState
    String consulContainerId;

    @ProvidedScenarioState
    ConsulKvPropertyValueProvider consulKvPropertyValueProvider;

    private final Map<String, HttpConulKeyValueRest> consulKvRest = new HashMap<>();

    private DockerClient dockerClient;

    private DockerConfig dockerConfig;

    private DockerSupport dockerSupport;

    public SELF consul_kv_value_provider_exist() {
        if (consulKvPropertyValueProvider == null) {
            consulKvPropertyValueProvider = new ConsulKvPropertyValueProvider(this.dockerClientSupport.getHttpContainerUrl(consulContainerId, 8500));
        }
        return self();
    }

    public SELF consul_is_started() {

        dockerClientSupport.pullImage("gliderlabs/consul:latest");

        Ports portBinding = new Ports();
        portBinding.bind(ExposedPort.tcp(8400), Ports.Binding(null));
        portBinding.bind(ExposedPort.tcp(8500), Ports.Binding(null));
        portBinding.bind(ExposedPort.udp(8600), Ports.Binding(null));
        CreateContainerCmd createContainerCmd = dockerClientSupport.getDockerClient().createContainerCmd("gliderlabs/consul")
                .withPortBindings(portBinding)
                .withExposedPorts(ExposedPort.tcp(8400), ExposedPort.tcp(8500), ExposedPort.udp(8600))
                .withCmd("agent", "-server", "-bootstrap", "-bind", "0.0.0.0", "-client", "0.0.0.0", "-data-dir", "/tmp/", "-ui");

        consulContainerId = this.startContainer(createContainerCmd, "consul");


        String containerUrl = dockerClientSupport.getHttpContainerUrl(consulContainerId, 8500);

        HttpConulKeyValueRest consulKeyValueRest = new HttpConulKeyValueRest(containerUrl);
        consulKvRest.put(consulContainerId, consulKeyValueRest);

        this.dockerSupport.waitUntilHttpRequestRespond(containerUrl + "/v1/status/leader", 5000, response -> {
            try {
                return response.body().string().contains(".");
            } catch (IOException e) {
                return false;
            }
        });
        return self();
    }

    public SELF add_key_$_with_value_$_to_consul(@Quoted String key, String value) {
        HttpConulKeyValueRest consulKeyValueRest = consulKvRest.get(consulContainerId);
        consulKeyValueRest.addKey(key, value);
        return self();
    }

    @BeforeScenario
    public void create_a_docker_client() {
        dockerClient = dockerClientSupport.getDockerClient();
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                PropertyResolver resolver = new PropertyResolver(new DockerConfigValueProvider(new SystemEnvValueProvider()));
                bind(DockerConfig.class).toInstance(resolver.createProxy(DockerConfig.class));
            }
        });
        dockerConfig = injector.getInstance(DockerConfig.class);
        dockerSupport = new DockerSupport(dockerConfig);
    }


    protected String startContainer(CreateContainerCmd createContainerCmd, String name) {
        if (createContainerCmd == null) {
            throw new IllegalArgumentException("createContainerCmd must be defined.");
        }
        CreateContainerResponse response = createContainerCmd.exec();

        dockerClient.startContainerCmd(response.getId()).exec();
        dockerClientSupport.addContainerIdToClean(response.getId());
        return response.getId();
    }

    private class HttpConulKeyValueRest {

        private String baseUrl;

        public HttpConulKeyValueRest(String baseUrl) {
            this.baseUrl = baseUrl + "/v1/kv/";
        }


        public String addKey(@Path("key") String key, @Body String value) {

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().put(RequestBody.create(MediaType.parse("text"), value)).url(baseUrl + key).build();
            LOGGER.info("Try to add key Request {}.", request);
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                LOGGER.error("Unable to put key value", e);
            } finally {
                if (response != null) {
                    try {
                        response.body().close();
                    } catch (IOException e) {
                        LOGGER.error("Unable to close request", e);
                    }
                }
            }
            return null;
        }
    }

}
