/****************************************************
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 ****************************************************/

package mihalyi.scaling.payara;

import com.hazelcast.core.HazelcastInstance;
import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.schedule.model.Schedule;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

/**
 *
 * @author Ondrej Mihalyi
 */
@Specializes
@ApplicationScoped
public class HazelcastMapDAO extends CachedScheduleDAO {

    @Resource(lookup = "payara/Hazelcast")
    private HazelcastInstance hazelcast;

    public HazelcastMapDAO() {
    }
    
    @Inject
    public HazelcastMapDAO(BootstrapData bootstrapData) {
        super(bootstrapData);
    }
    @Override
    protected void cacheSchedule(Schedule schedule) {
        super.cacheSchedule(schedule);
        hazelcast.getMap("cachedSchedules").put(schedule.getId(), schedule);
    }
    
}

