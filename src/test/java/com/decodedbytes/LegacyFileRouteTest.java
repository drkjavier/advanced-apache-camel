package com.decodedbytes;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
public class LegacyFileRouteTest {

    @Autowired
    CamelContext context;

    @EndpointInject("mock:result")
    protected MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    public void testMoveFile() throws Exception {

        // Setup de mock
        String expectedBody = "This is the input file";
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);

        // Tweak the route definition
        AdviceWith.adviceWith(context, "legacyFileMoveRoute", routeBuilder -> {
            routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
        });

        // Start the context and validate is mock is asserted
        context.start();
        mockEndpoint.assertIsSatisfied();

    }

    @Test
    public void testFileMoveByMockingFromEndpoint() throws Exception {

        // Setup de mock
        //String expectedBody = "This is the input data after mocking the from endpoint";
        String expectedBody = "OutboundNameAddress(name=Mike, address= 111  Toronto,  ON  M5F3D2)";
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);

        // Tweak the route definition
        AdviceWith.adviceWith(context, "legacyFileMoveRoute", routeBuilder -> {
            routeBuilder.replaceFromWith("direct:mockStart");
            routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
        });

        // Start the context and validate is mock is asserted
        context.start();
        producerTemplate.sendBody("direct:mockStart", "name, house_number, city, province, postal_code\n" +
                "Mike, 111, Toronto, ON, M5F3D2");
        mockEndpoint.assertIsSatisfied();

    }

}
