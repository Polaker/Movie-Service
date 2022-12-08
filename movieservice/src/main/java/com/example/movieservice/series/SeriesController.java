package com.example.movieservice.series;

import com.example.movieservice.exception.SeriesDoesNotExistsException;
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
@RequestMapping("/api/series")
public class SeriesController {

    private final SeriesService seriesService;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER' ,'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity getSeries() {
        return ResponseEntity.ok(seriesService.getSeries());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @PostMapping("/watch/{seriesId}")
    public ResponseEntity watchSeries(@PathVariable Long seriesId) throws SeriesDoesNotExistsException, UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(seriesService.watchSeries(seriesId,login));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @PostMapping("/rate/{seriesId}")
    public ResponseEntity<String> rateSeries(@PathVariable Long seriesId,
                                             @RequestParam int rating) throws UserDoesNotExistsException, SeriesDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        if (rating < 0 || rating > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating should be in range 1-10");
        }

        return seriesService.rateSeries(seriesId, rating, login);
    }

}
