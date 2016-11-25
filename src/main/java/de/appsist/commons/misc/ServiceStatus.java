package de.appsist.commons.misc;

/**
 * Enumeration of service status.
 * @author simon.schwantzer(at)im-c.de
 */
public enum ServiceStatus {
	/**
	 * The service is running within normal runtime parameters.
	 */
	OK,
	
	/**
	 * The service exceeds the normal runtime parameters but remains fully functional. 
	 */
	WARN,
	
	/**
	 * The service exceeds the normal runtime parameters and is limit in its functionality.
	 * The service should be restarted if this status remains. 
	 */
	ERROR,
	
	/**
	 * The status of the service is unknown. The service should be re-initialized.
	 */
	UNKNOWN
}
