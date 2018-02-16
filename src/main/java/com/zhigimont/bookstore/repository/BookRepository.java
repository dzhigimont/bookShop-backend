package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
   List<Book> findByTitleContaining(String keyword);
}
