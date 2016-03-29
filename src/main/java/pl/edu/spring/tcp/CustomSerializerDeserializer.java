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
package pl.edu.spring.tcp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.NestedIOException;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import java.io.*;

public class CustomSerializerDeserializer implements Serializer<Object>, Deserializer<Object>{

	private static final int SENDER_NAME_LENGTH = 10;

	private final ClassLoader classLoader;

	public CustomSerializerDeserializer() {
		this.classLoader = null;
	}

	public CustomSerializerDeserializer(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	protected final Log logger = LogFactory.getLog(this.getClass());
	private static final int ORDER_NUMBER_LENGTH = 3;

	private static final int MESSAGE_LENGTH_LENGTH = 6;


	private String pad(int desiredLength, int length) {
		return StringUtils.leftPad(Integer.toString(length), desiredLength, '0');
	}

	private String parseMessage(InputStream inputStream) throws IOException {
		String lengthString = parseString(inputStream, MESSAGE_LENGTH_LENGTH);
		int lengthOfMessage = Integer.valueOf(lengthString);

		String message = parseString(inputStream, lengthOfMessage);
		return message;
	}

	private String parseString(InputStream inputStream, int length) throws IOException {
		StringBuilder builder = new StringBuilder();

		int c;
		for (int i = 0; i < length; ++i) {
			c = inputStream.read();
			checkClosure(c);
			builder.append((char)c);
		}

		return builder.toString();
	}

	private String parseSenderName(InputStream inputStream) throws IOException {
		return parseString(inputStream, SENDER_NAME_LENGTH);
	}

	private int parseOrderNumber(InputStream inputStream) throws IOException {
		String value = parseString(inputStream, ORDER_NUMBER_LENGTH);
		return Integer.valueOf(value.toString());
	}

	protected void checkClosure(int bite) throws IOException {
		if (bite < 0) {
			logger.debug("Socket closed during message assembly");
			throw new IOException("Socket closed during message assembly");
		}
	}

	@Override
	public void serialize(Object object, OutputStream outputStream) throws IOException {
		if(!(object instanceof Serializable)) {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires a Serializable payload " + "but received an object of type [" + object.getClass().getName() + "]");
		} else {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
		}
	}

	public Object deserialize(InputStream inputStream) throws IOException {
		ConfigurableObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
		try {
			return objectInputStream.readObject();
		} catch (ClassNotFoundException var4) {
			throw new NestedIOException("Failed to deserialize object type", var4);
		}
	}
}
