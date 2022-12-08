package com.example.movieservice.admin;

import com.example.movieservice.exception.MovieAlreadyExistsException;
import com.example.movieservice.exception.SeriesAlreadyExistsException;
import com.example.movieservice.movie.Movie;
import com.example.movieservice.series.Series;
import com.example.movieservice.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<User> getUsers() {
        return adminService.getUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/movie")
    public ResponseEntity addNewMovie(@Valid @RequestBody Movie movie) throws MovieAlreadyExistsException {
        return ResponseEntity.ok(adminService.addMovie(movie));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/series")
    public ResponseEntity addNewSeries(@Valid @RequestBody Series series) throws SeriesAlreadyExistsException {
        return ResponseEntity.ok(adminService.addSeries(series));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/subscription/movie")
    public ResponseEntity addNewSubscriptionMovie(@Valid @RequestBody Movie movie) throws MovieAlreadyExistsException {
        return ResponseEntity.ok(adminService.addSubscriptionMovie(movie));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/subscription/series")
    public ResponseEntity addNewSubscriptionSeries(@Valid @RequestBody Series series) throws SeriesAlreadyExistsException {
        return ResponseEntity.ok(adminService.addSubscriptionSeries(series));
    }


}
