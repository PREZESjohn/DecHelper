package com.project.dechelper.controller;

import com.project.dechelper.service.InformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/info")
public class InformationCotroller {
    private final InformationService informationService;

}
