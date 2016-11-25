package de.appsist.commons.misc;

import java.util.HashSet;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

/**
 * Utility class to receive service status signals.
 * 
 * @author simon.schwantzer(at)im-c.de
 */
public class StatusSignalReceiver {
	private static final Logger logger = LoggerFactory.getLogger(StatusSignalReceiver.class);
	
	private final Set<Handler<ServiceStatusSignal>> signalHandlers;
	
	/**
	 * Creates the status signal receiver.
	 * @param eventBus Event bus to listen for service status signals. 
	 * @param config Configuration to extract channel information.
	 */
	public StatusSignalReceiver(EventBus eventBus, StatusSignalConfiguration config) {
		signalHandlers = new HashSet<Handler<ServiceStatusSignal>>();
		eventBus.registerHandler(config.getStatusSignalChannel(), new Handler<Message<JsonObject>>() {

			@Override
			public void handle(Message<JsonObject> message) {
				JsonObject body = message.body();
				try {
					ServiceStatusSignal signal = new ServiceStatusSignal(body);
					for (Handler<ServiceStatusSignal> signalHandler : signalHandlers) {
						try {
							signalHandler.handle(signal);
						} catch (Exception e) {
							logger.warn("Failed to forward service status signal to handler. ", e);
						}
					}
				} catch (IllegalArgumentException e) {
					logger.warn("Failed to decode service status signal.", e);
				}
			}
		});
	}
	
	/**
	 * Registers a handler for incoming status signals.
	 * @param signalHandler Handler to register for service status signals.
	 */
	public void registerSignalHandler(Handler<ServiceStatusSignal> signalHandler) {
		signalHandlers.add(signalHandler);
	}
	
	/**
	 * Removes a handler for service status signals.
	 * @param signalHandler Handler to remove.
	 */
	public void removeSignalHandler(Handler<ServiceStatusSignal> signalHandler) {
		signalHandlers.remove(signalHandler);
	}
	
}
