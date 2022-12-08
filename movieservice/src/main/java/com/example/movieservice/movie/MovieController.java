package com.example.movieservice.movie;

import com.example.movieservice.exception.MovieDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity getMovies() {
        return ResponseEntity.ok(movieService.getMovies());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @PostMapping("/watch/{movieId}")  // user can pick movie by ID and watch it
    public ResponseEntity watchMovie(@PathVariable Long movieId) throws MovieDoesNotExistsException, UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(movieService.watchMovie(movieId,login));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @PostMapping("/rate/{movieId}")
    public ResponseEntity<String> rateMovie(@PathVariable Long movieId,
                                            @RequestParam int rating) throws UserDoesNotExistsException, MovieDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        if (rating < 0 || rating > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating should be in range 1-10");
        }

        return movieService.rateMovie(movieId, rating ,login);
    }

}
