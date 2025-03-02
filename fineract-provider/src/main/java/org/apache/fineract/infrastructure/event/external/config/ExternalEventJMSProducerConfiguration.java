/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.event.external.config;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import org.apache.fineract.infrastructure.core.config.FineractProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;

@Configuration
@EnableIntegration
@ConditionalOnProperty(value = "fineract.events.external.producer.jms.enabled", havingValue = "true")
@Import(value = { ExternalEventJMSBrokerConfiguration.class })
public class ExternalEventJMSProducerConfiguration {

    @Autowired
    private DirectChannel outboundRequestsEvents;

    @Autowired
    private FineractProperties fineractProperties;

    @Bean
    public IntegrationFlow outboundFlowEvents(ConnectionFactory connectionFactory,
            @Qualifier("eventDestination") Destination eventDestination) {
        return IntegrationFlows.from(outboundRequestsEvents) //
                .handle(Jms.outboundAdapter(connectionFactory).destination(eventDestination)).get();
    }

}
