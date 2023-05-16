package com.document.Repository;

import com.document.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface DocumentRepository extends JpaRepository<Document, Long> {


    List<Document> findByDescriptionContainingIgnoreCase(String description);



}
