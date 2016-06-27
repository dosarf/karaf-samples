package org.apache.karaf.samples.client;

import org.apache.karaf.samples.api.Book;
import org.apache.karaf.samples.api.Library;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    public void start(BundleContext bundleContext) {
        System.out.println("Starting client");
        ServiceReference ref = bundleContext.getServiceReference(Library.class);
        if (ref != null) {
            Library library = (Library) bundleContext.getService(ref);
            library.addBook(new Book("20000 Lieux Sous La Mer", "Jules Vernes"));
            for (Book book : library.listBooks()) {
                System.out.println(book.getTitle() + "(" + book.getAuthor() + ")");
            }
        }
    }

    public void stop(BundleContext bundleContext) {
        System.out.println("Stopping client");
    }

}
