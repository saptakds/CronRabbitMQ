package com.saptak.config;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitlistener")
public class MessageListenerControl {

	@Autowired
	private SimpleMessageListenerContainer listenerContainer;

	private boolean enabled;

	public void pauseListener() {
		listenerContainer.stop();
	}

	public void resumeListener() {
		listenerContainer.start();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (enabled)
			resumeListener();
		else
			pauseListener();
	}
}
