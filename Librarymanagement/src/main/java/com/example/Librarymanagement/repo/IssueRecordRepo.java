package com.example.Librarymanagement.repo;

import com.example.Librarymanagement.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRecordRepo extends JpaRepository<IssueRecord , Long> {
}
