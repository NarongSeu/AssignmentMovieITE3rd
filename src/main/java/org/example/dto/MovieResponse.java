package org.example.dto;
import lombok.*;
import org.example.model.Movie;

import java.util.List;


@Setter
@Getter
public class MovieResponse{
    Integer page;
    List<Movie> results;
    Integer total_pages;
    Integer total_results;
}