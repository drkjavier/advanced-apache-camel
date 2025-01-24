package com.decodedbytes.routes;

import com.decodedbytes.beans.InboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.management.JMException;
import java.net.ConnectException;

@Component
public class NewRestRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet").host("0.0.0.0").port(8080).bindingMode(RestBindingMode.json).enableCORS(true);

        onException(JMSException.class, ConnectException.class)
                .handled(true)
                .log(LoggingLevel.INFO, "JMS connection could not be established");

        rest("masterclass")
                .produces("application/json")
                .post("nameAddress").type(InboundNameAddress.class).route().routeId("newRestRouteId")
                .log(LoggingLevel.INFO, String.valueOf(simple("${body}")))
                .to("direct:persistMessage")
                .wireTap("seda:sendToQueue")

                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .transform().simple("Message Processed: ${body}")
                .endRest();



        from("direct:persistMessage")
                .routeId("persistMessageRouteId")
                .to("jpa:"+InboundNameAddress.class.getName());

        from("seda:sendToQueue")
                .routeId("sendToQueueRouteId")
                .to("activemq:queue:nameaddressqueue");


//        from("timer:startBatchRoute?repeatCount=1&delay=1000")
//                .routeId("batchStartRouteId")
//                .to("controlbus:route?routeId=batchMessageRouteId&action=stop")
//                .to("controlbus:route?routeId=batchMessageRouteId&action=status")
//                .to("controlbus:route?routeId=activeMQSubscriberId&action=status")
//                .end();
//
//        from("timer:startBatchRoute?repeatCount=1&delay=5000")
//                .routeId("batchStopRouteId")
//                .to("controlbus:route?routeId=activeMQSusbcriberId&action=stop")
//                .to("controlbus:route?routeId=batchMessageRouteId&action=status")
//                .to("controlbus:route?routeId=activeMQSubscriberId&action=status")
//                .end();

    }
}