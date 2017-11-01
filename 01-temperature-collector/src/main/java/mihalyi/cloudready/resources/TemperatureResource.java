/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mihalyi.cloudready.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.cache.Cache;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("temperature")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class TemperatureResource {

    private static final String JSON_TEMPERATURE = "temperature";
    private static final String JSON_TIMESTAMP = "timestamp";

    @Inject
    private Cache<String, Integer> collectedTemperature;

    @GET
    @Path("/all")
    public Response allTemperatures(@QueryParam("count") Integer count) {
        Stream<Cache.Entry<String, Integer>> temperatureStream = StreamSupport
                .stream(collectedTemperature.spliterator(), false);
        JsonArrayBuilder temperatureArrayBuilder = Json.createArrayBuilder();
        temperatureStream.map(entry -> {
            return Json.createObjectBuilder()
                    .add(JSON_TIMESTAMP, entry.getKey())
                    .add(JSON_TEMPERATURE, entry.getValue());
        }).forEach(jsonObjectBuilder -> {
            temperatureArrayBuilder.add(jsonObjectBuilder);
        });
        final GenericEntity<JsonArray> entity = buildEntity(temperatureArrayBuilder);
        return Response.ok(entity).build();
    }

    private GenericEntity<JsonArray> buildEntity(final JsonArrayBuilder temperatureArrayBuilder) {
        return new GenericEntity<JsonArray>(temperatureArrayBuilder.build()) {
        };
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTemperature(@NotNull JsonObject temperature) {
        collectedTemperature.put(
                temperature.getString(JSON_TIMESTAMP),
                temperature.getInt(JSON_TEMPERATURE));
        return Response.ok().build();
    }

}
