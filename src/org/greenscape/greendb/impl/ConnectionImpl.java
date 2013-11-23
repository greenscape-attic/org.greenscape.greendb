package org.greenscape.greendb.impl;

import java.util.Map;

import org.greenscape.greendb.Connection;
import org.greenscape.greendb.GreenDBConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * 
 * @author Sheikh Sajid
 * 
 */
@Component(name = GreenDBConstants.SERVICE_NAME, configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = GreenDBConstants.CONFIG_PID, servicefactory = true)
public class ConnectionImpl implements Connection {

	private volatile Boolean initDB = false;
	private volatile String location = "local:/tmp/greendb";
	private volatile String username = "admin";
	private volatile String password = "admin";

	private ODatabaseDocument greenDb;

	@Override
	public ODatabaseDocument getDatabaseDocument() {
		return greenDb;
	}

	@Activate
	private void activate(Map<String, Object> properties) {
		if (properties != null && properties.get(GreenDBConstants.DB_INIT) != null
				&& !"".equals(properties.get(GreenDBConstants.DB_INIT))) {
			initDB = Boolean.valueOf((String) properties.get(GreenDBConstants.DB_INIT));
			if (initDB) {
				String loc = (String) properties.get(GreenDBConstants.DB_LOCATION);
				if (loc != null && !"".equals(loc)) {
					location = loc;
				}
				greenDb = initDB(location);
			}
		} else {
			if (initDB) {
				// earlier db was initialized, but now it is being disabled
				// TODO: notify others that db is not available anymore
				greenDb = initDB(location);
			}
		}
	}

	private ODatabaseDocument initDB(String dbPath) {
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbPath);
		if (!db.exists()) {
			db.create();
		} else {
			db.open(username, password);
		}
		return db;
	}
}
