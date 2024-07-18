package com.api_movie.api_movie.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api_movie.api_movie.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
