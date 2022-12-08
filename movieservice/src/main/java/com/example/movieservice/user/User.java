package com.example.movieservice.user;

import com.example.movieservice.movie.Movie;
import com.example.movieservice.series.Series;
import com.example.movieservice.wallet.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Movie_Service_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 3)
    private String password;

    private String role;

    @ManyToMany(mappedBy = "usersThatWatchedMovies")
    @JsonIgnore
    private Set<Movie> watchedMovies = new HashSet<>();

    @ManyToMany(mappedBy = "usersThatWatchedSeries")
    @JsonIgnore
    private Set<Series> watchedSeries = new HashSet<>();

    @NotBlank
    @Email(regexp = "^[_A-Za-z0-9+-]+(?:[.'’][_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(?:\\.[_A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")
    private String email;

    private boolean isSubscribed;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "fk_wallet_id")
    private Wallet wallet;

    // CONSTRUCTOR
    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isSubscribed = false;
    }

    // GETTERS
    public Long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Set<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public Set<Series> getWatchedSeries() {
        return watchedSeries;
    }

    public String getRole() {
        return role;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public Wallet getWallet() {
        return wallet;
    }


    // SETTERS
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWatchedMovies(Set<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public void setWatchedSeries(Set<Series> watchedSeries) {
        this.watchedSeries = watchedSeries;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Id.equals(user.Id) && username.equals(user.username) && password.equals(user.password) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, username, password, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
