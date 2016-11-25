package de.appsist.commons.misc;

import org.vertx.java.core.json.JsonObject;

/**
 * Configuration for status signaling.
 * 
 * @author simon.schwantzer(at)im-c.de
 */
public class StatusSignalConfiguration {
	private static final String DEFAULT_GROUP_ID = "de.appsist.service";
	private static final int DEFAULT_INTERVAL = 60000; // 60 seconds
	private static final String DEFAULT_SIGNAL_CHANNEL = "appsist:service:status";
	
	private JsonObject config;
	
	/**
	 * Creates a configuration wrapping given JSON object.
	 * @param config JSON object with a status signaling configuration. If a field is missing, the default values will be used.
	 */
	public StatusSignalConfiguration(JsonObject config) {
		this.config = config;
	}
	
	/**
	 * Creates a configuration with default values.
	 */
	public StatusSignalConfiguration() {
		this.config = new JsonObject();
	}
	
	/**
	 * Returns the group id for the signal.
	 * @return Group identifier.
	 */
	public String getGroupId() {
		return config.getString("groupId", DEFAULT_GROUP_ID);
	}
	
	/**
	 * Returns the interval for the signal.
	 * @return Interval in milliseconds.
	 */
	public int getInterval() {
		return config.getInteger("interval", DEFAULT_INTERVAL);
	}
	
	/**
	 * Returns the channel to publish signal on.
	 * @return Channel identifier.
	 */
	public String getStatusSignalChannel() {
		return config.getString("channel", DEFAULT_SIGNAL_CHANNEL);
	}
	
}
