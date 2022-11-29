package com.example.movieservice.series;

import com.example.movieservice.exception.SeriesDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

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

        series.getUsersThatWatchedSeries().add(userSeries); // Adding user to list in Series class
        series.setWatchCount(series.getWatchCount()+1);

        userSeries.getWatchedSeries().add(series); // Adding series to list in User class

        seriesRepository.save(series);
        userRepository.save(userSeries);
        return ResponseEntity.ok(series);
    }

}
