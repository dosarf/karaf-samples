package org.apache.karaf.samples.osgi.config.managed;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Activator implements BundleActivator {

    private ServiceRegistration<ManagedService> registration;

    public void start(BundleContext bundleContext) {
        ConfigDisplayer configDisplayer = new ConfigDisplayer();
        Hashtable<String, Object> serviceProperties = new Hashtable<String, Object>();
        serviceProperties.put(Constants.SERVICE_PID, "org.apache.karaf.samples.config");
        registration = bundleContext.registerService(ManagedService.class, configDisplayer, serviceProperties);
    }

    public void stop(BundleContext bundleContext) {
        if (registration != null) {
            registration.unregister();
        }
    }

    private final class ConfigDisplayer implements ManagedService {

        public void updated(Dictionary<String, ?> properties) {
            System.out.println("Config updated:");
            Enumeration<String> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                System.out.println(key + "=" + properties.get(key));
            }
        }

    }

}
