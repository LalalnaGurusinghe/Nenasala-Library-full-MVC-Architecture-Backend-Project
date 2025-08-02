package com.example.Librarymanagement.service;

import com.example.Librarymanagement.dto.BookDTO;
import com.example.Librarymanagement.entity.Book;
import com.example.Librarymanagement.repo.BookRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {

        return bookRepo.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book Not Found" + id));
    }

    public Book addBook(BookDTO bookDTO) {

        Book book = convertToEntity(bookDTO);
        return bookRepo.save(book);
    }

    public Book updateBookById(Long id, BookDTO bookDTO) {

        Book book = bookRepo.findById(id).orElseThrow((()->new RuntimeException("Book Not Found with id: " + id)));
        if(bookDTO.getTitle() != null) {
            book.setTitle(bookDTO.getTitle());
        }
        if(bookDTO.getAuthor() != null) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if(bookDTO.getIsbn() != null) {
            book.setIsbn(bookDTO.getIsbn());
        }
        if(bookDTO.getIsAvailable() != null) {
            book.setIsAvailable(bookDTO.getIsAvailable());
        }
        if(bookDTO.getQuantity() != null) {
            book.setQuantity(bookDTO.getQuantity());
        }
        return bookRepo.save(book);
    }

    public Book deleteBookById(Long id) {
        Book book = bookRepo.findById(id).orElseThrow(()->new RuntimeException("Book Not Found"));
        Book current = book;
        bookRepo.delete(book);
        return current;
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setIsAvailable(bookDTO.getIsAvailable());
        book.setQuantity(bookDTO.getQuantity());

        return book;
    }
}
