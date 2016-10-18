package org.apache.karaf.samples.tracker;

import org.apache.karaf.samples.api.Book;
import org.apache.karaf.samples.api.Library;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private ServiceTracker<Library, ServiceRegistration> tracker;

    public void start(BundleContext context) {
        tracker = new ServiceTracker(context, Library.class, null) {

            @Override
            public Library addingService(ServiceReference reference) {
                Library library = (Library) context.getService(reference);
                System.out.println("Library service has been added:");
                for (Book book : library.listBooks()) {
                    System.out.println(book.getTitle() + "(" + book.getAuthor() + ")");
                }
                return library;
            }

        };
        tracker.open();
    }

    public void stop(BundleContext context) {
        tracker.close();
    }

}
