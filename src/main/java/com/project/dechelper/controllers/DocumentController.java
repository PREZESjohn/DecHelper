package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/docs")
@Tag(name = "Document Crud")
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/all")
    @Operation(summary = "Get all informations")
    public ResponseEntity<List<Document>> getDocs() {
        return ResponseEntity.ok(documentService.getAllDocs());
    }

    @GetMapping("/get-subject/{subject}")
    @Operation(summary = "Get informations for subject")
    public ResponseEntity<List<Document>> getDocsBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(documentService.getAllDocsBySubject(subject));
    }

    @PutMapping("/add")
    @Operation(summary = "Save information")
    public ResponseEntity<?> saveDoc(@RequestBody Document document) {
        documentService.saveDoc(document);
        return ResponseEntity.ok("Document saved successfully");
    }

    @DeleteMapping("/delete-id/{id}")
    @Operation(summary = "Delete information by id")
    public ResponseEntity<?> deleteDoc(@PathVariable String id) {
        documentService.deleteDocById(id);
        return ResponseEntity.ok("Document deleted successfully");
    }

    @DeleteMapping("/delete-subject/{subject}")
    @Operation(summary = "Delete information by subject")
    public ResponseEntity<?> deleteDocBySubject(@PathVariable String subject) {
        documentService.deleteAllDocsBySubject(subject);
        return ResponseEntity.ok("Documents of "+subject+" deleted successfully");
    }
}
