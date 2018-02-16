package com.zhigimont.bookstore.service;

import com.zhigimont.bookstore.domain.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findOne(Long Id);

    Book save(Book book);

    List<Book> blurrySearch(String title);

    void removeOne(Long id);


}
