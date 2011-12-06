package gov.lbl.glamm.server;

import gov.lbl.glamm.server.ServerConfig.DbConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manages GLAMM server configurations as defined in the server_config.xml file.
 * @author jtbates
 *
 */
public class ConfigurationManager {

	public static final String DEFAULT_DOMAIN = "default";
	
	private static Map<String, ServerConfig> serverDomain2ServerConfig;
	private static Map<String, DbConfig> name2DbConfig;
	
	static {
		serverDomain2ServerConfig = new HashMap<String, ServerConfig>();
		name2DbConfig = new HashMap<String, DbConfig>();
	}
	
	private ConfigurationManager() {};
	
	private static DbConfig parseDbConfigElement(final Element element) {
		
		// validate and extract element attributes
		Map<DbConfig.Attribute, String> map = new HashMap<DbConfig.Attribute, String>();
		for(DbConfig.Attribute attribute : DbConfig.Attribute.values()) {
			if(!element.hasAttribute(attribute.toString()))
				throw new RuntimeException("Missing attribute " + attribute.toString());
			map.put(attribute, element.getAttribute(attribute.toString()));
		}
		
		return DbConfig.create(map);
	}
	
	private static ServerConfig parseServerConfigElement(final Element element) {
		
		Map<ServerConfig.Attribute, Object> map = new HashMap<ServerConfig.Attribute, Object>();
		for(ServerConfig.Attribute attribute : ServerConfig.Attribute.values()) {
			if(!element.hasAttribute(attribute.toString()) && attribute.isRequired())
				throw new RuntimeException("Missing required attribute " + attribute.toString());
			if(attribute == ServerConfig.Attribute.DB) {
				String dbName = element.getAttribute(ServerConfig.Attribute.DB.toString());
				DbConfig dbConfig = name2DbConfig.get(dbName);
				if(dbConfig == null)
					throw new RuntimeException("Could not find db configuration " + dbName);
				map.put(attribute, dbConfig);
			}
			else
				map.put(attribute, element.getAttribute(attribute.toString()));
		}
		return ServerConfig.create(map);
	}
	
	
	/**
	 * Initializes server-to-configuration mapping - should be called during servlet initialization.
	 * @param uri The uri of the server_config.xml file.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void init(final String uri) 
	throws ParserConfigurationException, IOException, SAXException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(uri);
		
		// parse the database configurations
		{
			String tagName = DbConfig.TAG_NAME;
			NodeList nodeList = doc.getElementsByTagName(tagName);
			if(nodeList == null || nodeList.getLength() == 0)
				throw new RuntimeException("No instances of " + tagName + " specified in " + uri);
			for(int i = 0; i < nodeList.getLength(); i++) {
				DbConfig dbConfig = parseDbConfigElement((Element) nodeList.item(i));
				name2DbConfig.put(dbConfig.getName(), dbConfig);
			}
		}
		
		// parse the server configurations
		{
			String tagName = ServerConfig.TAG_NAME;
			NodeList nodeList = doc.getElementsByTagName(tagName);
			if(nodeList == null || nodeList.getLength() == 0)
				throw new RuntimeException("No instances of " + tagName + " specified in " + uri);
			for(int i = 0; i < nodeList.getLength(); i++) {
				ServerConfig serverConfig = parseServerConfigElement((Element) nodeList.item(i));
				serverDomain2ServerConfig.put(serverConfig.getServerDomain(), serverConfig);
			}
		}	
	}
	
	/**
	 * Gets the server configuration for a given server host name.
	 * @param serverName The server host name.
	 * @return The configuration for the server host name.
	 */
	public static ServerConfig getServerConfigForServerName(final String serverName) {
		
		// can't do a direct lookup, as we're trying to do a fuzzy match 
		for(Entry<String, ServerConfig> entry : serverDomain2ServerConfig.entrySet()) {
			String serverDomain = entry.getKey();
			if(serverName.endsWith(serverDomain))
				return entry.getValue();
		}
		
		// if we've reached this point, the server configuration has not been specified
		// look for the default entry and throw an exception if one doesn't exist.
		ServerConfig defaultConfig = serverDomain2ServerConfig.get(DEFAULT_DOMAIN);
		if(defaultConfig == null)
			throw new RuntimeException("Could not find server configuration for " + serverName);
		return defaultConfig;
	}
}
