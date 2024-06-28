package com.directa24.main.challenge.service;

import com.directa24.main.challenge.dto.Movie;
import com.directa24.main.challenge.dto.ResponseApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DirectorServiceImp implements DirectorService {
    @Value("${app.external.service-url}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    @Autowired
    public DirectorServiceImp(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper =  mapper;
    }


    @Override
    public List<String> getDirectors(int threshold) {
        List<Movie> movies = fetchAllMovies();
        List<String> directorNames = getDirectorNames(movies, threshold);
        directorNames.sort(Comparator.naturalOrder());
        return directorNames;
    }

    private List<Movie> fetchAllMovies() {
        List<Movie> movies;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl.concat("/search?page=1"), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ResponseApi responseApi = mapper.readValue(response.getBody(), ResponseApi.class);
                movies = new ArrayList<>(responseApi.getData());
                int totalPages = responseApi.getTotalPages();
                for (int i = 2; i <= totalPages; i++) {
                    response = restTemplate.getForEntity(baseUrl.concat("/search?page=" + i), String.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        responseApi = mapper.readValue(response.getBody(), ResponseApi.class);
                        movies.addAll(responseApi.getData());
                    } else {
                        throw new RuntimeException("error fetching data from API, status code: " + response.getStatusCode());
                    }
                }
            } else {
                throw new RuntimeException("error fetching data from API, status code: " + response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error processing JSON response", e);
        } catch (RestClientException e) {
            throw new RuntimeException("error connecting to the external API", e);
        }
        return movies;
    }

    private List<String> getDirectorNames(List<Movie> movies, int threshold) {
        Map<String, Integer> moviesPerDirector = new HashMap<>();
        for (Movie movie : movies) {
            if(moviesPerDirector.containsKey(movie.getDirector())){
                int currentMovies = moviesPerDirector.get(movie.getDirector());
                moviesPerDirector.put(movie.getDirector(),currentMovies +1);
            }else {
                moviesPerDirector.put(movie.getDirector(),1);
            }
        }
        List<String> directorNames = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : moviesPerDirector.entrySet()){
            if(entry.getValue()>threshold){
                directorNames.add(entry.getKey());
            }
        }
        return directorNames;
    }
}

