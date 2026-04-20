package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Cast;

import java.util.List;

@Setter
@Getter
public class CasterResponse {

    private String id;
    private List<Cast> cast;
}