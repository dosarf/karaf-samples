package org.apache.karaf.samples.api;

import java.util.List;

public interface Library {

    void addBook(Book book);

    List<Book> listBooks();

}
