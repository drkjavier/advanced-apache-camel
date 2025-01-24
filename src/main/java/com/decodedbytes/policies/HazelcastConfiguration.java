package com.decodedbytes.policies;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HazelcastConfiguration {

    @Bean
    public HazelcastRoutePolicy getHazelcastRoutePolicy() {

        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        HazelcastRoutePolicy routePolicy = new HazelcastRoutePolicy(hz);

        routePolicy.setLockMapName("testLockMap");
        routePolicy.setLockKey("testLockKey");
        routePolicy.setLockValue("testLockValue");

        routePolicy.setTryLockTimeout(5, TimeUnit.SECONDS);

        return routePolicy;
    }

}
