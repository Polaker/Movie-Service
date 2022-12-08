package com.example.movieservice.movie;

import com.example.movieservice.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nameOfFilm;

    @NotBlank
    private String description;

    @NotBlank
    private String releaseYear;

    private double rating;

    private int watchCount;

    private boolean isSubscriptionOnly;

    @JsonIgnore
    @ElementCollection
    private List<Integer> usersRatingsList;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "users_watching_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> usersThatWatchedMovies;

    // CONSTRUCTOR
    public Movie() {
    }

    public Movie(String nameOfFilm,
                 String description,
                 String releaseYear) {
        this.nameOfFilm = nameOfFilm;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rating = 0.0;
        this.watchCount = 0;
        this.isSubscriptionOnly = false;
        this.usersThatWatchedMovies = new HashSet<>();
        this.usersRatingsList = new ArrayList<>();
    }

    // GETTERS
    public Long getId() {
        return id;
    }

    public Set<User> getUsersThatWatchedMovies() {
        return usersThatWatchedMovies;
    }

    public String getNameOfFilm() {
        return nameOfFilm;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public boolean isSubscriptionOnly() {
        return isSubscriptionOnly;
    }

    public double getRating() {
        return rating;
    }

    public List<Integer> getUsersRatingsList() {
        return usersRatingsList;
    }

    // SETTERS
    public void setUsersThatWatchedMovies(Set<User> usersThatWatchedMovies) {
        this.usersThatWatchedMovies = usersThatWatchedMovies;
    }

    public void setNameOfFilm(String nameOfFilm) {
        this.nameOfFilm = nameOfFilm;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public void setSubscriptionOnly(boolean subscriptionOnly) {
        isSubscriptionOnly = subscriptionOnly;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setUsersRatingsList(List<Integer> usersRatingsList) {
        this.usersRatingsList = usersRatingsList;
    }
}
