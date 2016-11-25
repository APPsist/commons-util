package de.appsist.commons.misc;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;

/**
 * Utility class to send service status signals.
 * 
 * @author simon.schwantzer(at)im-c.de
 */
public class StatusSignalSender {
	public static final ServiceStatus DEFAULT_SERVICE_STATUS = ServiceStatus.OK;
	
	private final Vertx vertx;
	private final String serviceId;
	private ServiceStatus serviceStatus;
	private Long timerId;
	private StatusSignalConfiguration config;
	
	/**
	 * Create the status signaling sender.
	 * The signaling is not started automatically, see {@link StatusSignalSender#start()} for details.
	 * @param serviceId Maven Artifact ID of the service, e.g. "mid".
	 * @param vertx Vertx runtime to use for signaling.
	 * @param config Configuration for the status signaling.
	 */
	public StatusSignalSender(String serviceId, Vertx vertx, StatusSignalConfiguration config) {
		this.vertx = vertx;
		this.serviceId = serviceId;
		this.config = config;
		this.serviceStatus = DEFAULT_SERVICE_STATUS;
		this.timerId = null;
	}
	
	/**
	 * Sets the service status to be send. If not set, the {@link StatusSignalSender#DEFAULT_STATUS} is sent.
	 * @param serviceStatus Service status to signal.
	 */
	public void setStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	/**
	 * Starts the signaling with the local parameters.
	 */
	public void start() {
		vertx.setPeriodic(config.getInterval(), new Handler<Long>() {
			
			@Override
			public void handle(Long id) {
				timerId = id;
				ServiceStatusSignal signal = new ServiceStatusSignal(config.getGroupId(), serviceId, serviceStatus);
				vertx.eventBus().publish(config.getStatusSignalChannel(), signal.asJson());
			}
		});
	}
	
	/**
	 * Stops the signaling.
	 */
	public void stop() {
		if (timerId != null) {
			vertx.cancelTimer(timerId);
			timerId = null;
		}
	}
}
