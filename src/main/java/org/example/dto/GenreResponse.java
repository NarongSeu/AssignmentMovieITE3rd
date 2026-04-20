package org.example.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.model.Genre;

import java.util.List;

@Setter
@Getter
public class GenreResponse {
    private List<Genre> genres;
}