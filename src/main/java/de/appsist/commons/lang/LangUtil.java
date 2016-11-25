package de.appsist.commons.lang;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;
import org.vertx.java.core.shareddata.SharedData;

/**
 * Utility class for language elements.
 * 
 * @author simon.schwantzer(at)im-c.de
 *
 */
public final class LangUtil {
	private static final Logger logger = LoggerFactory.getLogger(LangUtil.class);
	private static LangUtil INSTANCE;
	
	public static final String DEFAULT_LANGUAGE = "de_DE";
	
	private final Map<String, StringBundle> bundles;
	private final SharedData sharedData;
	
	private LangUtil(SharedData sharedData) {
		bundles = new HashMap<>();
		this.sharedData = sharedData;
	}
	
	/**
	 * Returns a instance of the utility.
	 * @param sharedData Shared data storage to retrieve bundles from.
	 * @return Utility instance.
	 */
	public static LangUtil getInstance(SharedData sharedData) {
		if (INSTANCE == null) {
			INSTANCE = new LangUtil(sharedData);
		}
		return INSTANCE;
	}
	
	/**
	 * Reads language bundles from a directory.
	 * This method is called once the system is initialized.
	 * @param sharedData Shared data repository to store bundles.
	 * @param propertiesFileDir Absolute path of a directory. All files in the directory will be imported.  
	 */
	public static void initializeBundles(SharedData sharedData, String propertiesFileDir) {
		Path bundlesPath = Paths.get(propertiesFileDir);
		try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(bundlesPath, new DirectoryStream.Filter<Path>() {

				@Override
				public boolean accept(Path entry) throws IOException {
					return Files.isRegularFile(entry);
				}
			});
			for (Path path : dirStream) {
				String languageId = path.getFileName().toString().split("\\.")[0];
				logger.info("Importing language bundle: " + languageId);

				Properties properties = new Properties();
				InputStream in = Files.newInputStream(path);
				properties.load(in);
								
				sharedData.removeMap("lang." + languageId); // purge old data
				ConcurrentMap<String, String> langMap = sharedData.getMap("lang." + languageId);
				
				for (String key : properties.stringPropertyNames()) {
					langMap.put(key, properties.getProperty(key));
				}
			}
		} catch (IOException e) {
			logger.warn("Failed read language bundle(s).", e);
		}
	}
	
	/**
	 * Returns the bundle for the default language.
	 * @return Localized string bundle.
	 */
	public StringBundle getBundle() {
		return getBundle(DEFAULT_LANGUAGE);
	}
	
	/**
	 * Returns the bundle for the given language.
	 * @param languageId Language identifier, e.g., "de_DE".
	 * @return Localized string bundle.
	 */
	public StringBundle getBundle(String languageId) {
		StringBundle bundle;
		if (bundles.containsKey(languageId)) {
			bundle = bundles.get(languageId);
		} else {
			ConcurrentMap<String, String> langMap = sharedData.getMap("lang." + languageId);
			bundle = new StringBundleImpl(languageId, langMap);
			bundles.put(languageId, bundle);
		}
		return bundle;
	}
}
