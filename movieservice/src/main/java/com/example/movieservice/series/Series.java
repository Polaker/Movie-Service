package com.example.movieservice.series;

import com.example.movieservice.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nameOfSeries;

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
            name = "users_watching_series",
            joinColumns = @JoinColumn(name = "series_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> usersThatWatchedSeries;


    // CONSTRUCTOR
    public Series() {
    }

    public Series(String nameOfSeries,
                  String description,
                  String releaseYear) {
        this.nameOfSeries = nameOfSeries;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rating = 0.0;
        this.watchCount = 0;
        this.isSubscriptionOnly = false;
        this.usersThatWatchedSeries = new HashSet<>();
        this.usersRatingsList = new ArrayList<>();
    }


    // GETTERS
    public String getNameOfSeries() {
        return nameOfSeries;
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

    public Set<User> getUsersThatWatchedSeries() {
        return usersThatWatchedSeries;
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

    public void setNameOfSeries(String nameOfSeries) {
        this.nameOfSeries = nameOfSeries;
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

    public void setUsersThatWatchedSeries(Set<User> usersThatWatchedSeries) {
        this.usersThatWatchedSeries = usersThatWatchedSeries;
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
