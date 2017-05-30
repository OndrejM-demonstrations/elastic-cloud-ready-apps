/** **************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *************************************************** */
package mihalyi.scaling.caching;

import com.hazelcast.config.CacheConfig;
import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.bootstrap.BootstrapDataProducer;
import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.persistence.ScheduleDAO;
import java.util.*;
import java.util.stream.StreamSupport;
import javax.cache.Cache;
import javax.cache.Caching;

/**
 *
 * @author Ondrej Mihalyi
 */
public class CachedScheduleDAO extends ScheduleDAO {

    public static final CachedScheduleDAO INSTANCE = new CachedScheduleDAO(new BootstrapDataProducer().load());

    Cache<String, Schedule> cachedSchedules;

    public CachedScheduleDAO(BootstrapData bootstrapData) {
        super(bootstrapData);
        CacheConfig<String, Schedule> config = new CacheConfig<>();
        config.setBackupCount(3);
        cachedSchedules = Caching.getCachingProvider().getCacheManager()
                .createCache("cachedSchedules", config);
    }

    @Override
    public List<Schedule> getAllSchedules(int limit) {
        List<Schedule> result = getAllSchedulesFromCache(limit);
        if (result.size() < limit) {
            result = super.getAllSchedules(limit);
            result.stream().forEach(this::cacheSchedule);
        }
        return result;
    }

    protected void cacheSchedule(Schedule schedule) {
        cachedSchedules.put(schedule.getId(), schedule);
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
