package org.example.service;

import org.example.dto.CasterResponse;
import org.example.dto.MovieDetailResponse;
import org.example.dto.MovieResponse;
import org.example.model.Genre;

import java.util.List;

public interface MovieService {
    MovieResponse getMoviesByTitleFromServer(Integer page,String title);

    MovieDetailResponse getMovieDetailsByTitleFromServer(String movieId);

    MovieResponse getPopularMovies(Integer page);

    List<Genre> getGenres();

    MovieResponse getMoviesByGenre(Integer page, String genreId);

    CasterResponse getMovieCaster(String movieId);
}