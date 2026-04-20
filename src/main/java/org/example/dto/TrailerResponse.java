package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Trailer;

import java.util.List;



@Setter
@Getter
public class TrailerResponse {
    List<Trailer> results;
}