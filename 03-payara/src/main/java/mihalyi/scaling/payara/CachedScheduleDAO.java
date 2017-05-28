/** **************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *************************************************** */
package mihalyi.scaling.payara;

import fish.payara.cdi.jsr107.impl.NamedCache;
import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.persistence.ScheduleDAO;
import java.util.*;
import java.util.stream.StreamSupport;
import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

/**
 *
 * @author Ondrej Mihalyi
 */
@Specializes
@ApplicationScoped
public class CachedScheduleDAO extends ScheduleDAO {

    @Inject
    @NamedCache(cacheName = "cachedSchedules")
    Cache<String, Schedule> cachedSchedules;

    public CachedScheduleDAO() {
    }

    @Inject
    public CachedScheduleDAO(BootstrapData bootstrapData) {
        super(bootstrapData);
        MutableConfiguration<String, Schedule> config = new MutableConfiguration<>();
    }

    @Override
    public List<Schedule> getAllSchedules(int limit) {
        List<Schedule> result = getAllSchedulesFromCache(limit);
        if (result.size() < limit) {
            result = super.getAllSchedules(limit);
            result.stream().forEach(schedule -> {
                cachedSchedules.put(schedule.getId(), schedule);
            });
        }
        return result;
    }

    private ArrayList<Schedule> getAllSchedulesFromCache(int limit) {
        ArrayList<Schedule> result = new ArrayList<>();
        StreamSupport.stream(cachedSchedules.spliterator(), false)
                .limit(limit)
                .map(entry -> entry.getValue())
                .forEach(result::add);
        return result;
    }

}
