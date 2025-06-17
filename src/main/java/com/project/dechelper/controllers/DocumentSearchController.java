package com.project.dechelper.controllers;

import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.DocumentSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
@Tag(name="Ai search controller")
public class DocumentSearchController {

    public final DocumentSearchService documentSearchService;

    @PostMapping("/sentence")
    public List<Document> findInfoBySentence(@RequestBody SentenceDTO sentence) {
        return documentSearchService.getRelevantData(sentence);
    }
}
