package com.example.movieservice.movie;

import com.example.movieservice.exception.MovieDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/watch/{movieId}")  // user can pick movie by ID and watch it
    public ResponseEntity watchMovie(@PathVariable Long movieId,
                                     @AuthenticationPrincipal Principal principal) throws MovieDoesNotExistsException, UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(movieService.watchMovie(movieId,login));
    }

}
