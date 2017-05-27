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
package io.microprofile.showcase.schedule.persistence;

import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.bootstrap.BootstrapDataProducer;
import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.model.adapters.LocalDateAdapter;
import io.microprofile.showcase.schedule.model.adapters.LocalTimeAdapter;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ScheduleDAO {

    public static final ScheduleDAO INSTANCE = new ScheduleDAO(new BootstrapDataProducer().load());

    BootstrapData bootstrapData;

    public ScheduleDAO(BootstrapData bootstrapData) {
        this.bootstrapData = bootstrapData;
        initStore();
    }

    public List<Schedule> getAllSchedules(int limit) {
        return scheduleMap.values().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private final AtomicInteger sequence = new AtomicInteger(0);

    private final Map<String, Schedule> scheduleMap = new ConcurrentHashMap<>();
    private final Map<String, String> venues = new ConcurrentHashMap<>();

    private void initStore() {
        Logger.getLogger(ScheduleDAO.class.getName()).log(Level.INFO, "Initialise schedule DAO from bootstrap data");

        final LocalDateAdapter dateAdapter = new LocalDateAdapter();
        final LocalTimeAdapter timeAdapter = new LocalTimeAdapter();

        bootstrapData.getSchedules()
                .forEach(bootstrap -> {

                    try {

                        String venueId = null;
                        for (final String key : venues.keySet()) {
                            final String v = venues.get(key);
                            if (v.equals(bootstrap.getVenue())) {
                                // existing venue
                                venueId = key;
                                break;
                            }
                        }

                        // generate a new key
                        if (null == venueId) {
                            venueId = String.valueOf(sequence.incrementAndGet());
                        }

                        final Schedule sched = new Schedule(
                                bootstrap.getId(),
                                bootstrap.getSessionId(),
                                bootstrap.getVenue(),
                                venueId,
                                dateAdapter.unmarshal(bootstrap.getDate()),
                                timeAdapter.unmarshal(bootstrap.getStartTime()),
                                Duration.ofMinutes(new Double(bootstrap.getLength()).longValue())
                        );

                        scheduleMap.put(bootstrap.getId(), sched);
                        venues.put(venueId, sched.getVenue());

                    } catch (final Exception e) {
                        System.out.println("Failed to parse bootstrap data: " + e.getMessage());
                    }

                });

    }

}
