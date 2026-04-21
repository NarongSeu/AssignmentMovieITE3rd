package org.example.utils;

import org.example.dto.CasterResponse;
import org.example.dto.GenreResponse;
import org.example.dto.MovieDetailResponse;
import org.example.dto.MovieResponse;
import org.example.model.Genre;
import org.example.model.Movie;
import org.example.model.ProductionCompany;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.ArrayList;
import java.util.List;


public class TableRenderer {
    public static void displayTableMoviesByTitle(MovieResponse movieResponse) {
        Table table = new Table(
                5, BorderStyle.UNICODE_BOX, ShownBorders.ALL
        );
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        String[] head = {"ID", "TITLE", "Release Date", "Rating", "Trailer"};
        for (int i = 0; i < head.length; i++) {
            table.addCell(head[i], cellStyle);
        }

        for (Movie movie : movieResponse.getResults()) {
            if (movie.getId() != null) {
                table.addCell(movie.getId());
            } else {
                table.addCell("N/A");
            }
            if (movie.getTitle() != null) {
                table.addCell(movie.getTitle());
            } else {
                table.addCell("N/A");
            }
            if (movie.getRelease_date() != null) {
                table.addCell(movie.getRelease_date());
            } else {
                table.addCell("N/A");
            }
            if (movie.getVote_average() != null) {
                table.addCell(movie.getVote_average().toString());
            } else {
                table.addCell("N/A");
            }
            if (movie.getTrailer() != null) {
                table.addCell("https://www.youtube.com/watch?v=" + movie.getTrailer().getKey());
            } else {
                table.addCell("N/A");
            }
        }
        System.out.println(table.render());
    }

    public static void displayTableMovieDetails(MovieDetailResponse movieDetailResponse, CasterResponse casterResponse) {
        Table table = new Table(
                2, BorderStyle.CLASSIC, ShownBorders.ALL
        );
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.CENTER);
        String[] head = {"ID", "TITLE", "BUDGET", "RELEASE DATE", "RUN TIME", "VOTE AVERAGE", "OVERVIEW", "GENRES", "ORIGIN COUNTRY", "PRODUCTION COMPANIES"};
        table.addCell(head[0],cellStyle);
        table.addCell(movieDetailResponse.getId());
        table.addCell(head[1],cellStyle);
        table.addCell(movieDetailResponse.getTitle());
        table.addCell(head[2],cellStyle);
        table.addCell(movieDetailResponse.getBudget().toString());
        table.addCell(head[3],cellStyle);
        table.addCell(movieDetailResponse.getRelease_date());
        table.addCell(head[4],cellStyle);
        table.addCell(movieDetailResponse.getRuntime().toString());
        table.addCell(head[5],cellStyle);
        table.addCell(movieDetailResponse.getVote_average().toString());
        table.addCell(head[6],cellStyle);
        table.addCell(movieDetailResponse.getOverview());

        table.addCell(head[7],cellStyle);
        List<String> genres = new ArrayList<>();
        for (Genre genre : movieDetailResponse.getGenres()) {
            genres.add(genre.getName());
        }
        table.addCell(genres.toString());

        table.addCell(head[8],cellStyle);
        List<String> originCountry = new ArrayList<>();
        for (String s : movieDetailResponse.getOrigin_country()) {
            originCountry.add(s);
        }
        table.addCell(originCountry.toString());

        table.addCell(head[9],cellStyle);
        List<String > productionCompany = new ArrayList<>();
        for (ProductionCompany pc : movieDetailResponse.getProduction_companies()) {
            productionCompany.add(pc.getName());
        }
        table.addCell(productionCompany.toString());
        System.out.println(table.render());
        System.out.println();
        Table table2 = new Table(
                3, BorderStyle.CLASSIC, ShownBorders.ALL
        );
        String[] headCaster = {"NAME", "GENDER", "CHARACTER"};
        table2.addCell(headCaster[0],cellStyle);
        table2.addCell(headCaster[1],cellStyle);
        table2.addCell(headCaster[2],cellStyle);
        for (int i = 0; i < casterResponse.getCast().size(); i++) {
            table2.addCell(casterResponse.getCast().get(i).getName());
            table2.addCell(casterResponse.getCast().get(i).getGender().equals("0")? "Unknown" :
                    casterResponse.getCast().get(i).getGender().equals("1")? "Female" : "Male" );
            table2.addCell(casterResponse.getCast().get(i).getCharacter());
        }
        System.out.println(table2.render());

    }
}