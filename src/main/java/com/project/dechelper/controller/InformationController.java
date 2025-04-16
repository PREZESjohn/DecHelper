package com.project.dechelper.controller;

import com.project.dechelper.model.Information;
import com.project.dechelper.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/info")
public class InformationController {
    private final InformationService informationService;

    @GetMapping("/all")
    public ResponseEntity<List<Information>> getInformations() {
        return ResponseEntity.ok(informationService.getAllInfo());
    }

    @GetMapping("/{subject}")
    public ResponseEntity<List<Information>> getInfomrationsBySubject(@PathVariable String subject) {
        return ResponseEntity.ok(informationService.getAllInfoBySubject(subject));
    }

    @PutMapping("/add")
    public ResponseEntity<Information> saveInformation(@RequestBody Information information) {
        return ResponseEntity.ok(informationService.saveInfo(information));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInformation(@PathVariable String id) {
        informationService.deleteInfoById(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{subject}")
    public ResponseEntity<?> deleteInformationBySubject(@PathVariable String subject) {
        informationService.deleteAllInfoBySubject(subject);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
