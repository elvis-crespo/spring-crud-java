package com.api_movie.api_movie.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.api_movie.api_movie.dtos.MovieDTO;
import com.api_movie.api_movie.dtos.MoviePageResponse;

public interface MovieService {

    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException;

    MovieDTO getMovie(Integer movieId);

    List<MovieDTO> getAllMovies();

    MovieDTO updateMovie(Integer movieId, MovieDTO movieDTO, MultipartFile file) throws IOException;

    String deleteMovie(Integer movieId) throws IOException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir);
}
