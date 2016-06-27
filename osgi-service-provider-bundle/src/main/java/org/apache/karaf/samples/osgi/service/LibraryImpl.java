package org.apache.karaf.samples.osgi.service;

import org.apache.karaf.samples.api.Book;
import org.apache.karaf.samples.api.Library;

import java.util.ArrayList;
import java.util.List;

public class LibraryImpl implements Library {

    private ArrayList<Book> books = new ArrayList<Book>();

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> listBooks() {
        return books;
    }

}
