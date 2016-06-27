package org.apache.karaf.samples.osgi.service;

import org.apache.karaf.samples.api.Library;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) {
        LibraryImpl library = new LibraryImpl();
        serviceRegistration = bundleContext.registerService(Library.class, library, null);
    }

    public void stop(BundleContext bundleContext) {
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
    }

}
