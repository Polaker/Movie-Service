package com.example.movieservice.series;

import com.example.movieservice.exception.SeriesDoesNotExistsException;
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
@RequestMapping("/api/series")
public class SeriesController {

    private final SeriesService seriesService;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity getSeries() {
        return ResponseEntity.ok(seriesService.getSeries());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/watch/{seriesId}")
    public ResponseEntity watchSeries(@PathVariable Long seriesId,
                                      @AuthenticationPrincipal Principal principal) throws SeriesDoesNotExistsException, UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(seriesService.watchSeries(seriesId,login));
    }
}
