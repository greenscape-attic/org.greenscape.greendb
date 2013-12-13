/**
 * 
 */
package org.greenscape.greendb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;

/**
 * @author Sheikh Sajid
 * 
 */

public interface Connection {
	ODatabaseDocument getDatabaseDocument();
}
