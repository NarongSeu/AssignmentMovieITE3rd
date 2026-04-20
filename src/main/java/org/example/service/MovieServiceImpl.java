package org.example.service;

import org.example.dto.*;
import org.example.model.Genre;
import org.example.model.Movie;
import org.example.model.Trailer;
import org.example.utils.ApiTest;
import tools.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static jdk.javadoc.doclet.DocletEnvironment.ModuleMode.API;

public class MovieServiceImpl implements MovieService{
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private ApiTest API;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public MovieResponse getMoviesByTitleFromServer(Integer page,String title)  {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String URL = String.format(
                "%s/search/movie?api_key=%s&query=%s&page=%d",
                API.BASE_URL,API.API_KEY,encodedTitle,page
        );
        MovieResponse allMovie = getFromApi(URL,MovieResponse.class);
        attachTrailers(allMovie);
        return allMovie;
    }

    @Override
    public MovieDetailResponse getMovieDetailsByTitleFromServer(String movieId) {
        String encodedString = URLEncoder.encode(movieId, StandardCharsets.UTF_8);
        String URL = String.format( "%s/movie/%s",  API.BASE_URL,encodedString );
        return getFromApi(URL,MovieDetailResponse.class);
    }
    @Override
    public MovieResponse getPopularMovies(Integer page) {
        String URL = String.format( "%s/movie/popular?lanuage=en-US&page=%d", API.BASE_URL,page);
        if(page > 500){ return null; }
        MovieResponse movieResponse =  getFromApi(URL,MovieResponse.class);
        attachTrailers(movieResponse);
        return movieResponse;
    }

    @Override
    public List<Genre> getGenres() {
        String URL = String.format("%s/genre/movie/list", API.BASE_URL);
        GenreResponse genreResponse = getFromApi(URL,GenreResponse.class);
        return genreResponse.getGenres();
    }
    @Override
    public MovieResponse getMoviesByGenre(Integer page, String genreId) {
        String encodedString = URLEncoder.encode(genreId, StandardCharsets.UTF_8);
        String URL = String.format( "%s/discover/movie?with_genres=%s&page=%d", API.BASE_URL, encodedString, page );
        MovieResponse movieResponse = getFromApi(URL,MovieResponse.class);
        attachTrailers(movieResponse);
        return movieResponse;
    }
    @Override
    public CasterResponse getMovieCaster(String movieId) {
        String encodedString = URLEncoder.encode(movieId, StandardCharsets.UTF_8);
        String URL = String.format( "%s/movie/%s/credits", API.BASE_URL,encodedString );
        return getFromApi(URL,CasterResponse.class);
    }
    private <T> T getFromApi(String url, Class<T> clazz) {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + API.BEARER_TOKEN,
                        "Accept", "application/json")
                .uri(URI.create(url)) .GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), clazz);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Invalid ID!");
        }
    }

    private void attachTrailers(MovieResponse movieResponse) {
        for (Movie movie : movieResponse.getResults()) {
            String url = String.format( "%s/movie/%s/videos", API.BASE_URL, movie.getId() );
            TrailerResponse trailerData = getFromApi(url, TrailerResponse.class);
            if (trailerData.getResults() != null) {
                for (Trailer t : trailerData.getResults()) {
                    if ("Trailer".equals(t.getType())) {
                        movie.setTrailer(t);
                        break;
                    }
                }
            }
        }
    }
}