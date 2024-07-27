package com.api_movie.api_movie.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api_movie.api_movie.dtos.MovieDTO;
import com.api_movie.api_movie.entities.Movie;
import com.api_movie.api_movie.exceptions.FileExistsException;
import com.api_movie.api_movie.exceptions.MovieNotFoundException;
import com.api_movie.api_movie.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final FileService fileService;

     @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseURL;


    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException{
        // Upload file
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("File already exists! Please enter abother file name.");
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        // Set the value of field 'poster' as filename
        movieDTO.setPoster(uploadedFileName);

        //Map DTO
        Movie movie = new Movie(
            null,   //movieId
            movieDTO.getTitle(),
            movieDTO.getDirector(),
            movieDTO.getStudio(),
            movieDTO.getMovieCast(),
            movieDTO.getReleaseYear(),
            movieDTO.getPoster()
        );

        // Save the movie object
        Movie savedMovie = movieRepository.save(movie);

        // Generate the posterURL
        String posterURL = baseURL + "/file/" + uploadedFileName;

        // Map movie object to dto object and return it
        MovieDTO response = new MovieDTO(
            savedMovie.getMovieId(),
            savedMovie.getTitle(),
            savedMovie.getDirector(),
            savedMovie.getStudio(),
            savedMovie.getMovieCast(),
            savedMovie.getReleaseYear(),
            savedMovie.getPoster(),
            posterURL
        );
        
        return response;
    }

 
    @Override
    public MovieDTO getMovie(Integer movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));

        // Generate the posterURL
        String posterURL = baseURL + "/file/" + movie.getPoster();

        return new MovieDTO(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterURL
        );
    }
    

    @Override
    public List<MovieDTO> getAllMovies() {

        List<Movie> movies = movieRepository.findAll();

        List<MovieDTO> movieDTOs = new ArrayList<>();

        for (Movie movie : movies) {
            String posterURL = baseURL + "/file/" + movie.getPoster();
            MovieDTO movieDTO = new MovieDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterURL
            );
            movieDTOs.add(movieDTO);
        }
        
        return movieDTOs;
    }

    @Override
    public MovieDTO updateMovie(Integer movieId, MovieDTO movieDTO, MultipartFile file) throws IOException {
        // Retrieve the movie from the repository
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));


        String fileName = mv.getPoster();

        // If a new poster file is provided, update the poster
        if(file != null && !file.isEmpty()) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }               

        movieDTO.setPoster(fileName);

        Movie movie = new Movie(
            mv.getMovieId(),
            movieDTO.getTitle(),
            movieDTO.getDirector(),
            movieDTO.getStudio(),
            movieDTO.getMovieCast(),
            movieDTO.getReleaseYear(),
            movieDTO.getPoster()
        );

        // Save the updated movie
        Movie updatedMovie = movieRepository.save(movie);

        String posterURL = baseURL + "/file/" + fileName;

        // Map the updated movie object to a DTO object and return it
        return new MovieDTO(
            movie.getMovieId(),
            movie.getTitle(),
            movie.getDirector(),
            movie.getStudio(),
            movie.getMovieCast(),
            movie.getReleaseYear(),
            movie.getPoster(),
            posterURL
        );
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id = " + movieId));        
        
        Integer id = mv.getMovieId();
        
        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));    

        movieRepository.delete(mv);

        return "Movie deleted with id = " + id;
    }

}
