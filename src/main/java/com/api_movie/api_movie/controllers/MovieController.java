package com.api_movie.api_movie.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api_movie.api_movie.dtos.MovieDTO;
import com.api_movie.api_movie.dtos.MoviePageResponse;
import com.api_movie.api_movie.exceptions.EmptyFileException;
import com.api_movie.api_movie.services.MovieService;
import com.api_movie.api_movie.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    
    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDTO) throws IOException, EmptyFileException{
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty! Please send another file.");
        }
        MovieDTO dto = convertTMovieDTO(movieDTO);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieByIdHandler(@PathVariable Integer movieId){
        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMoviesHandler(){
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDTO> updateMovieHandler(@RequestPart MultipartFile file,
                                                        @RequestPart String movieDTOObj,
                                                        @PathVariable Integer movieId) throws IOException{
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        MovieDTO dto = convertTMovieDTO(movieDTOObj);
        return new ResponseEntity<>(movieService.updateMovie(movieId, dto, file), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException{
        return new ResponseEntity<>(movieService.deleteMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(@RequestParam(defaultValue =  AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize){
        
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));

    }   

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(@RequestParam(defaultValue =  AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                                @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                                                @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String Dir){
        
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSort(pageNumber, pageSize, sortBy, Dir));

    }

    private MovieDTO convertTMovieDTO(String MovieDTOObject) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(MovieDTOObject, MovieDTO.class);
    }
}
