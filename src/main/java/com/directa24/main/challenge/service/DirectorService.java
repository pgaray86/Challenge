package com.directa24.main.challenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface DirectorService {
    List<String> getDirectors(int threshold);
}
