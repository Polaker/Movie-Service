package com.example.movieservice.admin;

import com.example.movieservice.exception.MovieAlreadyExistsException;
import com.example.movieservice.exception.SeriesAlreadyExistsException;
import com.example.movieservice.movie.Movie;
import com.example.movieservice.movie.MovieRepository;
import com.example.movieservice.series.Series;
import com.example.movieservice.series.SeriesRepository;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    @Autowired
    public AdminService(UserRepository userRepository, MovieRepository movieRepository, SeriesRepository seriesRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Movie addMovie(Movie movie) throws MovieAlreadyExistsException {
        if (movieRepository.findMovieBynameOfFilm(movie.getNameOfFilm()).isPresent()) {
            throw new MovieAlreadyExistsException("This movie is already in our database!");
        }
        return movieRepository.save(movie);
    }

    @Transactional
    public Series addSeries(Series series) throws SeriesAlreadyExistsException {
        if (seriesRepository.findOneBynameOfSeries(series.getNameOfSeries()).isPresent()) {
            throw new SeriesAlreadyExistsException("This series is already in our database!");
        }
        return seriesRepository.save(series);
    }

    @Transactional
    public Movie addSubscriptionMovie(Movie movie) throws MovieAlreadyExistsException {
        if (movieRepository.findMovieBynameOfFilm(movie.getNameOfFilm()).isPresent()) {
            throw new MovieAlreadyExistsException("This movie is already in our database!");
        }
        movie.setSubscriptionOnly(true);
        return movieRepository.save(movie);
    }

    @Transactional
    public Series addSubscriptionSeries(Series series) throws SeriesAlreadyExistsException {
        if (seriesRepository.findOneBynameOfSeries(series.getNameOfSeries()).isPresent()) {
            throw new SeriesAlreadyExistsException("This series is already in our database!");
        }
        series.setSubscriptionOnly(true);
        return seriesRepository.save(series);
    }
}

