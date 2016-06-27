package org.apache.karaf.samples.osgi.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.util.Dictionary;
import java.util.Enumeration;

public class Activator implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        // get configuration admin service
        ServiceReference serviceReference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        if (serviceReference != null) {
            ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);
            if (configurationAdmin != null) {
                Configuration configuration = configurationAdmin.getConfiguration("org.apache.karaf.samples.osgi");
                Dictionary<String, Object> properties = configuration.getProperties();
                Enumeration<String> keys = properties.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    System.out.println(key + "=" + properties.get(key));
                }
            }
        }
    }

    public void stop(BundleContext bundleContext) {
        // nothing to do
    }

}
