package com.document.Controller;


import com.document.Model.Document;
import com.document.Repository.DocumentRepository;
import com.document.Service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
    public class DocumentController {


    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    DocumentService documentService;


    @RequestMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("pdf", new Document());
        return "dashboard";
    }

    @PostMapping("/upload")
    public String uploadSubmit(@ModelAttribute("pdf") @Valid Document pdf,
                               BindingResult bindingResult,
                               Model model,
                               MultipartFile pdfFile) throws IOException {

        if (bindingResult.hasErrors()) {
            return "dashboard";
        }


        if (!pdfFile.isEmpty()) {
            pdf.setPdf(pdfFile.getBytes());
        }

        documentRepository.save(pdf);

        model.addAttribute("message", "PDF uploaded successfully!");
        return "user/success";
    }

    @GetMapping("/pdfs")
    public String list(Model model) {
        List<Document> pdfs = documentRepository.findAll();
        model.addAttribute("pdfs", pdfs);
        return "admin/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(value = "id") long id, Model model) {
        System.out.println("im inside edit controller");
        Document pdfs = documentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid pdf Id:" + id));
        model.addAttribute("pdfs", pdfs);
        return "admin/edit";
    }

    @PostMapping("/update/{id}")
    public String updatePdf(@ModelAttribute Document pdf, @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile, RedirectAttributes redirectAttributes) throws IOException {

        if (pdfFile != null && !pdfFile.isEmpty()) {

            byte[] bytes = pdfFile.getBytes();
            pdf.setPdf(bytes);
        }
        documentRepository.save(pdf);
        redirectAttributes.addFlashAttribute("message", "PDF updated successfully!");
        return "redirect:/pdfs";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Document pdfs = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pdf ID: " + id));
        documentRepository.delete(pdfs);
        return "redirect:/pdfs";
    }

//    @GetMapping("/pdf/{id}/download")
//    public ResponseEntity<ByteArrayResource> downloadPDF(@PathVariable("id") long id) throws IOException {
//
//        Optional<Document> pdfOptional = documentRepository.findById(id);
//        if (pdfOptional.isPresent()) {
//            Document pdf = pdfOptional.get();
//
//            byte[] pdfData = pdf.getPdf();
//
//            ByteArrayResource resource = new ByteArrayResource(pdfData);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getName() + ".pdf");
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(pdfData.length)
//                    .body(resource);
//        } else {
//
//            return ResponseEntity.notFound().build();
//        }
//    }
//


    @GetMapping("/pdf/{id}/download")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable("id") long id) throws IOException {
        Optional<Document> pdfOptional = documentRepository.findById(id);
        if (pdfOptional.isPresent()) {
            Document pdf = pdfOptional.get();
            byte[] pdfData = pdf.getPdf();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getName() + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return ResponseEntity.ok().headers(headers).body(pdfData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @GetMapping("/api/documents/search")
    public String searchDocumentsByName(@RequestParam("name") String description, Model model) {
        List<Document> pdfs = documentRepository.findByDescriptionContainingIgnoreCase(description);
        model.addAttribute("pdfs", pdfs);
        return "admin/search";
    }

//    @GetMapping(value = "/api/documents/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
//    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
//        System.out.println("im inside the download");
//        Optional<Document> document = documentRepository.findById(id);
//        if (document.isPresent()) {
//            byte[] pdfContents = document.get().getPdf();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDisposition(ContentDisposition.builder("attachment")
//                    .filename(document.get().getName())
//                    .build());
//            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


}



