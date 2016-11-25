package de.appsist.commons.lang;

import java.util.Map;

import org.vertx.java.core.json.JsonObject;

/**
 * Bundle of strings. A bundle is localized.
 * 
 * @author simon.schwantzer(at)im-c.de
 */
public interface StringBundle {
	/**
	 * Returns an entry.
	 * @param id Identifier of the entry.
	 * @return Entry for the given identifier, <code>null</code> if no such entry exists.
	 */
	public String getString(String id);
	
	/**
	 * Returns an entry or a default value if the entry does not exist.
	 * @param id Identifier of the entry.
	 * @param defaultValue Value to return if the entry does not exist.
	 * @return Entry or default value.
	 */
	public String getString(String id, String defaultValue);
	
	/**
	 * Returns a map containing all entries.
	 * @return Immutable map of the bundle.
	 */
	public Map<String, String> asMap();
	
	/**
	 * Returns a JSON object containing all entries.
	 * @return JSON object representing the bundle.
	 */
	public JsonObject asJson();
}
