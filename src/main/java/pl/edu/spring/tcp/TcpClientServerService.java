/*
 * Copyright 2015 the original author or authors.
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
package pl.edu.spring.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.stereotype.Service;
import pl.edu.spring.application.controller.MessageFrameController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@EnableIntegration
@IntegrationComponentScan
@Service
public class TcpClientServerService {

	MessageFrameController controller;

	@Autowired
	private Config.Gateway gateway;

	@Autowired
	private AbstractServerConnectionFactory serverCF;

	public String sendFile(File fileToSend) throws IOException {
		TestingUtilities.waitListening(serverCF, 10000L);
		InputStream in = new FileInputStream(fileToSend);
		byte[] bytes = new byte[(int) fileToSend.length()];
		FileData fileData = new FileData(bytes, fileToSend.getName());
		String result = null;
		while (in.read(fileData.getByteArray()) != -1) {
			result = gateway.viaTcp(fileData);
		}
		in.close();
		return result;
	}

	public String sendTextMessage(String message) throws IOException {
		TestingUtilities.waitListening(serverCF, 10000L);
		return gateway.viaTcp(message);
	}

	public void receive(FileData in) {
		controller.receiveMessage(in);
	}

	public void receive(String stringReceived) {
		controller.receiveMessage(stringReceived);
	}

	public MessageFrameController getController() {
		return controller;
	}

	public void setController(MessageFrameController controller) {
		this.controller = controller;
	}

}
