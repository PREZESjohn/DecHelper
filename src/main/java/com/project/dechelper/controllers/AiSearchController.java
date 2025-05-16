package com.project.dechelper.controllers;

import com.project.dechelper.model.Information;
import com.project.dechelper.model.SentenceDTO;
import com.project.dechelper.services.AiSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
@Tag(name="Ai search controller")
public class AiSearchController {

    public final AiSearchService aiSearchService;

    @PostMapping("/sentence")
    public List<Information> findInfoBySentence(@RequestBody SentenceDTO sentence) {
        return aiSearchService.getRelevantData(sentence);
    }
}
