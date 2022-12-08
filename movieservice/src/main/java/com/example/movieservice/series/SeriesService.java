package com.example.movieservice.series;

import com.example.movieservice.exception.SeriesDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final UserRepository userRepository;

    @Autowired
    public SeriesService(SeriesRepository seriesRepository,
                         UserRepository userRepository) {
        this.seriesRepository = seriesRepository;
        this.userRepository = userRepository;
    }

    public List<Series> getSeries() {
        return seriesRepository.findAll();
    }


    public ResponseEntity watchSeries(Long seriesId, String login) throws SeriesDoesNotExistsException, UserDoesNotExistsException {

        if (seriesRepository.findById(seriesId).isEmpty()) {
            throw new SeriesDoesNotExistsException("Series with provided ID does not exists!");
        }

        if (userRepository.findUserByUsername(login).isEmpty()) {
            throw new UserDoesNotExistsException("User with provided username does not exists!");
        }

        Series series = seriesRepository.findById(seriesId).get();
        User userSeries = userRepository.findUserByUsername(login).get();

        if (series.isSubscriptionOnly()) {
            if (userSeries.isSubscribed()) {
                series.getUsersThatWatchedSeries().add(userSeries); // Adding user to list in Series class
                series.setWatchCount(series.getWatchCount()+1);
                userSeries.getWatchedSeries().add(series); // Adding series to list in User class
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This series is only available to subscribed users!");
            }
        } else {
            series.getUsersThatWatchedSeries().add(userSeries);
            series.setWatchCount(series.getWatchCount()+1);
            userSeries.getWatchedSeries().add(series);
        }

        seriesRepository.save(series);
        userRepository.save(userSeries);
        return ResponseEntity.ok(series);
    }

    public ResponseEntity<String> rateSeries(long seriesId, int rating, String login) throws UserDoesNotExistsException, SeriesDoesNotExistsException {
        User user = checkUser(login);
        Series series = checkSeries(seriesId);

        if (!user.getWatchedSeries().contains(series)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can rate only series that you have watched!");
        }
        series.getUsersRatingsList().add(rating);

        int sumOfRatings = 0;
        for (Integer number : series.getUsersRatingsList()) {
            sumOfRatings+= number;
        }

        double newRating = (double) sumOfRatings/series.getUsersRatingsList().size();
        newRating = round(newRating, 1);

        series.setRating(newRating);
        seriesRepository.save(series);

        return ResponseEntity.ok("Series rating added!");
    }

    // PRIVATE METHODS
    private User checkUser(String login) throws UserDoesNotExistsException {
        Optional<User> optionalUser = userRepository.
                findUserByUsername(login);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistsException("User with provided username does not exists!");
        }

        return optionalUser.get();
    }

    private Series checkSeries(long seriesId) throws SeriesDoesNotExistsException {
        Optional<Series> optionalMovie = seriesRepository.
                findById(seriesId);
        if (optionalMovie.isEmpty()) {
            throw new SeriesDoesNotExistsException("Series is not in our database!");
        }

        return optionalMovie.get();
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
