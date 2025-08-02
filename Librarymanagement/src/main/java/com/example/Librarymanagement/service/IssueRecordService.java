package com.example.Librarymanagement.service;

import com.example.Librarymanagement.entity.Book;
import com.example.Librarymanagement.entity.IssueRecord;
import com.example.Librarymanagement.entity.User;
import com.example.Librarymanagement.repo.BookRepo;
import com.example.Librarymanagement.repo.IssueRecordRepo;
import com.example.Librarymanagement.repo.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IssueRecordService {

    private final IssueRecordRepo issueRecordRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    public IssueRecordService(IssueRecordRepo issueRecordRepo, BookRepo bookRepo, UserRepo userRepo) {
        this.issueRecordRepo = issueRecordRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }


    public IssueRecord issueTheBook(Long bookId) {

        Book book = bookRepo.findById(bookId)
                .orElseThrow(()->new RuntimeException("Book Not Found"));

        if(book.getQuantity() <=0 || !book.getIsAvailable()){
            throw  new RuntimeException("Book is not available");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setIssueDate(LocalDate.now());
        issueRecord.setDueDate(LocalDate.now().plusDays(14));
        issueRecord.setIsReturned(false);
        issueRecord.setUser(user);
        issueRecord.setBook(book);

        book.setQuantity(book.getQuantity() - 1);
        if(book.getQuantity() == 0) {
            book.setIsAvailable(false);
        }

        bookRepo.save(book);
        return issueRecordRepo.save(issueRecord);
    }

    public IssueRecord returnTheBook(Long issueRecordId){
        IssueRecord issueRecord = issueRecordRepo.findById(issueRecordId)
                .orElseThrow(()->new RuntimeException("Issue Not Found"));

        if(issueRecord.getIsReturned()){
            throw new RuntimeException("Book is already returned");
        }

        Book book = issueRecord.getBook();
        book.setQuantity(book.getQuantity() + 1);
        book.setIsAvailable(true);
        bookRepo.save(book);

        issueRecord.setReturnDate(LocalDate.now());
        issueRecord.setIsReturned(true);

        return issueRecordRepo.save(issueRecord);
    }
}
