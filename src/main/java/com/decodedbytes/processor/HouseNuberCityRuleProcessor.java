package com.decodedbytes.processor;

import com.decodedbytes.beans.InboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class HouseNuberCityRuleProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        InboundNameAddress nameAddress = exchange.getIn().getBody(InboundNameAddress.class);

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("ksession-rule");

        kieSession.insert(nameAddress);

        kieSession.fireAllRules();
        kieSession.destroy();

        if(null == nameAddress.getPreferred()) {
            System.out.println("Throw an exception here if required");
        }

    }
}
