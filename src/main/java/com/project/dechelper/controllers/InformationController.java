package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.services.InformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/info")
@Tag(name = "Info Crud")
public class InformationController {
    private final InformationService informationService;

    @GetMapping("/all")
    @Operation(summary = "Get all informations")
    public ResponseEntity<List<Information>> getInformations() {
        return ResponseEntity.ok(informationService.getAllInfo());
    }

    @GetMapping("/get-subject/{subject}")
    @Operation(summary = "Get informations for subject")
    public ResponseEntity<List<Information>> getInformationsBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(informationService.getAllInfoBySubject(subject));
    }

    @PutMapping("/add")
    @Operation(summary = "Save information")
    public ResponseEntity<Information> saveInformation(@RequestBody Information information) {
        return ResponseEntity.ok(informationService.saveInfo(information));
    }

    @DeleteMapping("/delete-id/{id}")
    @Operation(summary = "Delete information by id")
    public ResponseEntity<?> deleteInformation(@PathVariable String id) {
        informationService.deleteInfoById(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-subject/{subject}")
    @Operation(summary = "Delete information by subject")
    public ResponseEntity<?> deleteInformationBySubject(@PathVariable String subject) {
        informationService.deleteAllInfoBySubject(subject);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
