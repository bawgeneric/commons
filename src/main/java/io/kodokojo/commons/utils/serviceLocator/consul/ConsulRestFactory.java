package io.kodokojo.commons.utils.servicelocator.consul;

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