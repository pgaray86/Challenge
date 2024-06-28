package com.directa24.main.challenge.controller;

import com.directa24.main.challenge.service.DirectorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping()
    ResponseEntity<?> getDirectors(@RequestParam(value = "threshold") Integer threshold) {
        Map<String, Object> response = new HashMap<>();
        var directors = directorService.getDirectors(threshold);
        response.put("directors",directors);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
