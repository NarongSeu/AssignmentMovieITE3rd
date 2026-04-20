package org.example;

import org.example.dto.MovieResponse;
import org.example.model.Genre;
import org.example.service.MovieService;
import org.example.service.MovieServiceImpl;
import org.example.utils.TableRenderer;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String ICON_MOVIE = "🎬";
    private static final String ICON_SEARCH = "🔍";
    private static final String ICON_POPULAR = "⭐";
    private static final String ICON_GENRE = "🎭";
    private static final String ICON_EXIT = "🚪";
    private static final String ICON_NEXT = "➡️";
    private static final String ICON_PREV = "⬅️";
    private static final String ICON_BACK = "🔙";
    private static final String ICON_DETAIL = "📋";
    private static final String ICON_PAGE = "📄";
    private static final String ICON_ID = "🆔";
    private static final String ICON_WARNING = "⚠️";
    private static final String ICON_SUCCESS = "✅";
    private static final String ICON_MENU = "📌";
    private static final String ICON_ARROW = "👉";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MovieService movieService = new MovieServiceImpl();

        printLogo();
        System.out.println("""
                ╔════════════════════════════════════════════════════════════════╗
                ║                                                                    ║
                ║    ███╗   ███╗ ██████╗ ██╗   ██╗██╗███████╗                      ║
                ║    ████╗ ████║██╔═══██╗██║   ██║██║██╔════╝                      ║
                ║    ██╔████╔██║██║   ██║██║   ██║██║█████╗                        ║
                ║    ██║╚██╔╝██║██║   ██║╚██╗ ██╔╝██║██╔══╝                        ║
                ║    ██║ ╚═╝ ██║╚██████╔╝ ╚████╔╝ ██║███████╗                      ║
                ║    ╚═╝     ╚═╝ ╚═════╝   ╚═══╝  ╚═╝╚══════╝                      ║
                ║                                                                    ║
                ║              ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗     ║
                ║              ██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║     ║
                ║              ███████╗█████╗  ███████║██████╔╝██║     ███████║     ║
                ║              ╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║     ║
                ║              ███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║     ║
                ║              ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝     ║
                ║                                                                    ║
                ║                    🎬 WELCOME TO CINEMA SEARCH 🎬                  ║
                ╚════════════════════════════════════════════════════════════════╝
                """);
        while(true) {
            printMenuHeader();
            System.out.println(ICON_MENU + " [1] " + ICON_SEARCH + " Search Movies By Title");
            System.out.println(ICON_MENU + " [2] " + ICON_POPULAR + " Popular Movies");
            System.out.println(ICON_MENU + " [3] " + ICON_GENRE + " Search By Genres");
            System.out.println(ICON_MENU + " [E] " + ICON_EXIT + " Exit");
            System.out.print(ICON_ARROW + " Enter Option : ");
            String opt = scanner.nextLine().toLowerCase();

            switch (opt) {
                case "1" -> searchMoviesByTitle(scanner, movieService);
                case "2" -> showPopularMovies(scanner, movieService);
                case "3" -> searchMoviesByGenre(scanner, movieService);
                case "e" -> {
                    System.out.println(ICON_SUCCESS + " Thank you for using Movie Search! Goodbye! " + ICON_MOVIE);
                    System.exit(0);
                }
                default -> System.out.println(ICON_WARNING + " Invalid Option! Please try again.");
            }
        }
    }

    private static void searchMoviesByTitle(Scanner scanner, MovieService movieService) {
        System.out.println("\n" + ICON_SEARCH + "============ Search By Title ===========");
        System.out.print(ICON_ARROW + " Enter Title : ");
        String title = scanner.nextLine();
        Integer currentPage = 1;
        boolean isSearching = true;

        while (isSearching) {
            MovieResponse movieResponse = movieService.getMoviesByTitleFromServer(currentPage, title);
            TableRenderer.displayTableMoviesByTitle(movieResponse);
            displayPaginationInfo(movieResponse);

            String listOpt = getUserNavigationChoice(scanner);
            NavigationResult result = handleNavigation(listOpt, scanner, movieResponse, currentPage, movieService);

            if (result.shouldExit) {
                isSearching = false;
                if (result.exitProgram) System.exit(0);
            } else {
                currentPage = result.newPage;
            }
        }
    }

    private static void showPopularMovies(Scanner scanner, MovieService movieService) {
        System.out.println("\n" + ICON_POPULAR + "============ Popular Movies ===========");
        Integer currentPage = 1;
        boolean isSearching = true;

        while (isSearching) {
            try {
                MovieResponse movieResponse = movieService.getPopularMovies(currentPage);
                TableRenderer.displayTableMoviesByTitle(movieResponse);
                displayPaginationInfo(movieResponse); // FIXED: Now using the helper method

                String listOpt = getUserNavigationChoice(scanner);
                NavigationResult result = handleNavigation(listOpt, scanner, movieResponse, currentPage, movieService);

                if (result.shouldExit) {
                    isSearching = false;
                    if (result.exitProgram) System.exit(0);
                } else {
                    currentPage = result.newPage;
                }
            } catch (RuntimeException e) {
                System.out.println(ICON_WARNING + " " + e.getMessage());
                break;
            }
        }
    }

    private static void searchMoviesByGenre(Scanner scanner, MovieService movieService) {
        System.out.println("\n" + ICON_GENRE + "============ Search By Genres ===========");
        List<Genre> genres = movieService.getGenres();

        System.out.println("\n📚 Available Genres:");
        for (int i = 0; i < genres.size(); i++) {
            System.out.printf("  %2d. %s %s\n", (i + 1), getGenreIcon(genres.get(i).getName()), genres.get(i).getName());
        }

        int choice = -1;
        while (true) {
            System.out.print("\n" + ICON_ARROW + " Choose Genre (1-" + genres.size() + "): ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > genres.size()) {
                    System.out.println(ICON_WARNING + " Invalid choice! Please select between 1 and " + genres.size());
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println(ICON_WARNING + " Please enter a valid number!");
            }
        }

        Genre selected = genres.get(choice - 1);
        String genreId = String.valueOf(selected.getId());

        Integer currentPage = 1;
        boolean isSearching = true;

        while (isSearching) {
            MovieResponse movieResponse = movieService.getMoviesByGenre(currentPage, genreId);
            TableRenderer.displayTableMoviesByTitle(movieResponse);
            System.out.println(ICON_GENRE + " Genre: " + selected.getName() + " | " +
                    ICON_PAGE + " Page " + movieResponse.getPage() + " of " +
                    movieResponse.getTotal_pages() + " | Total Results: " + movieResponse.getTotal_results());

            String listOpt = getUserNavigationChoice(scanner);
            NavigationResult result = handleNavigation(listOpt, scanner, movieResponse, currentPage, movieService);

            if (result.shouldExit) {
                isSearching = false;
                if (result.exitProgram) System.exit(0);
            } else {
                currentPage = result.newPage;
            }
        }
    }

    private static void showMovieDetails(Scanner scanner, MovieService movieService, String movieId) {
        // FIXED: Added validation for empty movie ID
        if (movieId == null || movieId.trim().isEmpty()) {
            System.out.println(ICON_WARNING + " Movie ID cannot be empty!");
            return;
        }

        try {
            System.out.println(ICON_DETAIL + " Fetching movie details...");
            TableRenderer.displayTableMovieDetails(
                    movieService.getMovieDetailsByTitleFromServer(movieId),
                    movieService.getMovieCaster(movieId)
            );
            System.out.println("\n" + ICON_BACK + " Press Enter to go back...");
            scanner.nextLine();
        } catch (RuntimeException e) {
            System.out.println(ICON_WARNING + " Invalid Movie ID! Please check and try again.");
        }
    }

    private static String getUserNavigationChoice(Scanner scanner) {
        System.out.println("\n" + "──────────────────────────────────────────────────");
        System.out.println(ICON_NEXT + " [n] Next Page    " + ICON_PREV + " [p] Previous Page    " + ICON_BACK + " [b] Back");
        System.out.println(ICON_PAGE + " [g] Go to Page   " + ICON_DETAIL + " [md] Movie Details   " + ICON_EXIT + " [e] Exit");
        System.out.print(ICON_ARROW + " Choose an Option: ");
        return scanner.nextLine().toLowerCase();
    }

    private static NavigationResult handleNavigation(String option, Scanner scanner,
                                                     MovieResponse response, int currentPage,
                                                     MovieService movieService) {
        switch (option) {
            case "n" -> {
                return new NavigationResult(false, false, updatePageNumber(response, currentPage, currentPage + 1));
            }
            case "p" -> {
                return new NavigationResult(false, false, updatePageNumber(response, currentPage, currentPage - 1));
            }
            case "g" -> {
                try {
                    System.out.print(ICON_ARROW + " Enter Page Number: ");
                    Integer goToPage = Integer.parseInt(scanner.nextLine());
                    return new NavigationResult(false, false, updatePageNumber(response, currentPage, goToPage));
                } catch (NumberFormatException e) {
                    System.out.println(ICON_WARNING + " Invalid number!");
                    return new NavigationResult(false, false, currentPage);
                }
            }
            case "md" -> {
                System.out.print(ICON_ID + " Enter Movie ID: ");
                String movieId = scanner.nextLine();
                showMovieDetails(scanner, movieService, movieId);
                return new NavigationResult(false, false, currentPage);
            }
            case "b" -> {
                return new NavigationResult(true, false, 1);
            }
            case "e" -> {
                return new NavigationResult(true, true, 1);
            }
            default -> {
                System.out.println(ICON_WARNING + " Invalid option!");
                return new NavigationResult(false, false, currentPage);
            }
        }
    }

    private static Integer updatePageNumber(MovieResponse movieResponse, Integer currentPage, Integer pageNum) {
        if (pageNum < 1) return 1;
        if (pageNum > movieResponse.getTotal_pages()) return movieResponse.getTotal_pages();
        if (pageNum > 500) return 500;
        return pageNum;
    }

    private static void displayPaginationInfo(MovieResponse movieResponse) {
        System.out.println(ICON_PAGE + " Page " + movieResponse.getPage() + " of " +
                movieResponse.getTotal_pages() + " | Total Results: " + movieResponse.getTotal_results());
    }

    private static void printLogo() {
        String logo = """
                
                ╔══════════════════════════════════════════════════════════════╗
                ║                         🎬 MOVIE SEARCH 🎬                    ║
                ║                    Your Ultimate Movie Database               ║
                ╚══════════════════════════════════════════════════════════════╝
                
                """;
        System.out.println(logo);
    }

    private static void printMenuHeader() {
        System.out.println("\n" + "══════════════════════════════════════════════════");
        System.out.println("              🎬 MAIN MENU 🎬");
        System.out.println("══════════════════════════════════════════════════");
    }

    private static String getGenreIcon(String genreName) {
        if (genreName == null) return "🎬"; // FIXED: Added null check
        return switch (genreName.toLowerCase()) {
            case "action" -> "💥";
            case "comedy" -> "😄";
            case "drama" -> "🎭";
            case "horror" -> "👻";
            case "romance" -> "💕";
            case "sci-fi", "science fiction" -> "🚀";
            case "thriller" -> "🔪";
            case "animation" -> "🐭";
            case "documentary" -> "📹";
            default -> "🎬";
        };
    }

    // Helper record for navigation results
    private record NavigationResult(boolean shouldExit, boolean exitProgram, int newPage) {}
}