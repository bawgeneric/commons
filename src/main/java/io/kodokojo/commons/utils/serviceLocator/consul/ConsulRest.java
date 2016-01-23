package io.kodokojo.commons.utils.serviceLocator.consul;

import com.google.gson.JsonArray;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

interface ConsulRest {
    @GET("/v1/catalog/service/{serviceName}")
    JsonArray getServices(@Path("serviceName") String serviceName, @Query("tag") String tags);
}
