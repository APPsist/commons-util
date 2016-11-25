package de.appsist.commons.misc;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.vertx.java.core.json.JsonObject;

/**
 * Model for a service status signals.
 * @author simon.schwantzer(at)im-c.de
 */
public class ServiceStatusSignal {
	private final JsonObject json;
	private final ServiceStatus serviceStatus;
	private final Date created;
	
	public ServiceStatusSignal(String groupId, String serviceId, ServiceStatus serviceStatus) {
		Calendar now = Calendar.getInstance();
		String currentDateTime = DatatypeConverter.printDateTime(now);
		created = now.getTime();
		this.serviceStatus = serviceStatus;
		json = new JsonObject()
			.putString("groupId", groupId)
			.putString("serviceId", serviceId)
			.putString("status", serviceStatus.toString())
			.putString("created", currentDateTime);
	}
	
	/**
	 * Creates a service status signal wrapping the given JSON object.
	 * @param json JSON object encoding a service status signal.
	 * @throws IllegalArgumentException The given JSON object is invalid.
	 */
	public ServiceStatusSignal(JsonObject json) throws IllegalArgumentException {
		this.json = json;
		ServiceStatusSignal.validateJson(json);
		serviceStatus = ServiceStatus.valueOf(json.getString("status"));
		created = DatatypeConverter.parseDateTime(json.getString("created")).getTime();
		
	}
	
	private static final void validateJson(JsonObject json) throws IllegalArgumentException {
		if (json.getString("groupId") == null) {
			throw new IllegalArgumentException("Missing group identifier [groupId].");
		}
		if (json.getString("serviceId") == null) {
			throw new IllegalArgumentException("Missing service identifier [serviceId].");
		}
		if (json.getString("status") == null) {
			throw new IllegalArgumentException("Missing serice status [status].");
		}
		if (json.getString("created") == null) {
			throw new IllegalArgumentException("Missing creation date time informationen [created].");
		}
	}
	
	/**
	 * Returns the group identifier of the signal.
	 * @return Group identifier.
	 */
	public String getGroupId() {
		return json.getString("groupId");
	}
	
	/**
	 * Returns the service identifier of the status update.
	 * @return Service identifier, e.g., "mid".
	 */
	public String getServiceId() {
		return json.getString("serviceId");
	}
	
	/**
	 * Returns the status of the service. 
	 * @return  Status of the service.
	 */
	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}
	
	/**
	 * Returns the JSON object wrapped by this model.
	 * @return JSON object encoding the service status signal.
	 */
	public JsonObject asJson() {
		return json;
	}
	
	/**
	 * Returns the date time of the signals creation.
	 * @return Date time of creation.
	 */
	public Date getCreated() {
		return created;
	}
	
}
