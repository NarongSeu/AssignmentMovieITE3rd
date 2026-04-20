package org.example.dto;

import lombok.*;
import org.example.model.Genre;
import org.example.model.ProductionCompany;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class MovieDetailResponse {
    private String id;
    private String title;
    private Double budget;
    private String release_date;
    private Double runtime;
    private Double vote_average;
    private String overview;
    private List<Genre> genres;
    private List<String> origin_country;
    private List<ProductionCompany> production_companies;
    private List<CasterResponse> casterResponses;
}
