package com.document.Service;

import com.document.Model.Document;
import com.document.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {


    @Autowired
    DocumentRepository documentRepository;

    public void save(Document booking) {
        documentRepository.save(booking);
    }

    public List<Document> listAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findClientById(Long id) {
        return documentRepository.findById(id);
    }



}
