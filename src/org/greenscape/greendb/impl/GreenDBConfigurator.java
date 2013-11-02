package org.greenscape.greendb.impl;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.OStorageException;

/**
 * The configurator class for the GreenDB database.
 * 
 * @author Sheikh Sajid
 * 
 */
public class GreenDBConfigurator implements ManagedService {
	private final static String DB_INIT = "greendb.init";
	private final static String DB_LOCATION = "greendb.location";
	private final static String DB_USERNAME = "greendb.username";
	private final static String DB_PASSWORD = "greendb.password";

	private volatile Boolean initDB = false;
	private volatile String location = "local:/tmp/greendb";
	private volatile String username = "admin";
	private volatile String password = "admin";

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties != null && properties.get(DB_INIT) != null && !"".equals(properties.get(DB_INIT))) {
			initDB = Boolean.valueOf((String) properties.get(DB_INIT));
			if (initDB) {
				String loc = (String) properties.get(DB_LOCATION);
				if (loc != null && !"".equals(loc)) {
					location = loc;
				} else {
					throw new ConfigurationException(DB_LOCATION, "Cannot be null");
				}
				String user = (String) properties.get(DB_USERNAME);
				if (user != null && !"".equals(user)) {
					username = user;
				}
				String passwd = (String) properties.get(DB_PASSWORD);
				if (passwd != null && !"".equals(passwd)) {
					password = passwd;
				}
				initDB(location, username, password);
				// TODO: publish service to get db handle
			}
		} else {
			if (initDB) {
				// earlier db was initialized, but now it is being disabled
				// TODO: notify others that db is not available anymore
			}
		}
	}

	private void initDB(String dbPath, String userName, String password) {
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbPath);
		try {
			db.open(userName, password);
		} catch (OStorageException e) {
			db.create();
		} finally {
			db.close();
		}
	}

}
