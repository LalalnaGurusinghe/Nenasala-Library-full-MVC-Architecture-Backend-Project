package com.example.Librarymanagement.controller;

import com.example.Librarymanagement.entity.IssueRecord;
import com.example.Librarymanagement.service.IssueRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issuerecords")
public class IssueController {

    private final IssueRecordService issueRecordService;

    public IssueController(IssueRecordService issueRecordService) {
        this.issueRecordService = issueRecordService;
    }

    @PostMapping("/issue/{bookId}")
    public ResponseEntity<IssueRecord> issueTheBook(@PathVariable Long bookId){

        return ResponseEntity.ok(issueRecordService.issueTheBook(bookId));
    }

    @PostMapping("/return/{issueRecordId}")
    public ResponseEntity<IssueRecord> returnTheBook(@PathVariable Long issueRecordId){
        return ResponseEntity.ok(IssueRecordService.returnTheBook(issueRecordId));
    }
}
