package org.example.model;

import lombok.*;
import org.example.dto.GenreResponse;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Genre {
    private Long id;
    private String name;
    private List<Movie> movies;

}
