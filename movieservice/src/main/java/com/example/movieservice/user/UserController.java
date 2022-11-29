package com.example.movieservice.user;

import com.example.movieservice.exception.UserAlreadyExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        userService.registerUser(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/watched/movies")
    public ResponseEntity showWatchedMovies(@AuthenticationPrincipal Principal principal) throws UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(userService.showWatchedMovies(login));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/watched/series")
    public ResponseEntity showWatchedSeries(@AuthenticationPrincipal Principal principal) throws UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return ResponseEntity.ok(userService.showWatchedSeries(login));
    }
}
