package com.decodedbytes.routes;

import com.decodedbytes.policies.DependentRoutePolicy;
import com.decodedbytes.processor.HouseNuberCityRuleProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.RoutePolicy;
import org.springframework.stereotype.Component;

@Component
public class QueueReceiverRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        RoutePolicy dependentRoutePolicy = new DependentRoutePolicy("batchMessageRouteId", "activeMQSubscriberId");

        from("activemq:queue:nameaddressqueue")
                .routeId("activeMQSubscriberId")
                .process(new HouseNuberCityRuleProcessor())
                .log(LoggingLevel.INFO, ">>>>>>>>>>> Message Received from queue: ${body}");

    }
}
