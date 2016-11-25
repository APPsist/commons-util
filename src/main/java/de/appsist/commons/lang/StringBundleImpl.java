package de.appsist.commons.lang;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

/**
 * Default implementation for a string bundle.
 *
 * @author simon.schwantzer(at)im-c.de
 */
public class StringBundleImpl implements StringBundle {
	private static final Logger logger = LoggerFactory.getLogger(StringBundleImpl.class);

	private Map<String, String> entries;
	
	protected StringBundleImpl( String languageId, Map<String, String> entries) {
		if (entries.isEmpty()) {
			logger.warn("Initialized empty string bundle for language: " + languageId);
		}
		this.entries = entries;
	}

	@Override
	public String getString(String id) {
		return entries.get(id);
	}

	@Override
	public String getString(String id, String defaultValue) {
		String value = entries.get(id);
		return value != null ? value : defaultValue;
	}

	@Override
	public Map<String, String> asMap() {
		return Collections.unmodifiableMap(entries);
	}
	
	@Override
	public JsonObject asJson() {
		JsonObject json = new JsonObject();
		for (Entry<String, String> entry : entries.entrySet()) {
			json.putString(entry.getKey(), entry.getValue());
		}
		return json;
	}
}
