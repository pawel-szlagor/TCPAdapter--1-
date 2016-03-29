/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.edu.spring.tcp.support;

import org.apache.log4j.Logger;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.integration.test.util.SocketUtils;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.GenericXmlContextLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gunnar Hillert
 */
public class CustomContextLoader extends GenericXmlContextLoader {

    private static final Logger LOGGER = Logger.getLogger(CustomContextLoader.class);

    @Override
    protected void loadBeanDefinitions(GenericApplicationContext context,
                                       MergedContextConfiguration mergedConfig) {

        int availableServerSocket = SocketUtils.findAvailableServerSocket(3456);

        final Map<String, Object> sockets = new HashMap<String, Object>();
        sockets.put("availableServerSocket", availableServerSocket);
        sockets.put("ipAddress", "localhost");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Available server socket: " + availableServerSocket);
        }

        final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

        context.getEnvironment().getPropertySources().addLast(propertySource);
        super.loadBeanDefinitions(context, mergedConfig);
    }

}
