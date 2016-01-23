package io.kodokojo.commons.utils.serviceLocator.consul;

import com.google.gson.Gson;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ConsulRestFactory {

    public static ConsulRest build(String baseUrl, Gson gson) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("baseUrl must be defined.");
        }
        if (gson == null) {
            throw new IllegalArgumentException("gson must be defined.");
        }
        RestAdapter restAdater = new RestAdapter.Builder().setEndpoint(baseUrl).setConverter(new GsonConverter(gson)).build();
        ConsulRest consulRest = restAdater.create(ConsulRest.class);
        return consulRest;
    }


}
