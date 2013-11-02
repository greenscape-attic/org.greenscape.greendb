package org.greenscape.greendb.impl;

import java.util.Properties;

import org.greenscape.greendb.GreenDBConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

public class Activator implements BundleActivator {

	private ServiceRegistration registration;

	@Override
	public void start(BundleContext context) throws Exception {
		GreenDBConfigurator svrConn = new GreenDBConfigurator();

		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, GreenDBConstants.CONFIG_PID);
		registration = context.registerService(ManagedService.class.getName(), svrConn, properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (registration != null) {
			registration.unregister();
			registration = null;
		}
	}

}
