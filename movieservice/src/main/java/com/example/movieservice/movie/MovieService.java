package com.example.movieservice.movie;

import com.example.movieservice.exception.MovieDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public ResponseEntity watchMovie(Long movieId, String login) throws MovieDoesNotExistsException, UserDoesNotExistsException {

        if (movieRepository.findById(movieId).isEmpty()) {
            throw new MovieDoesNotExistsException("Movie with provided ID does not exists!");
        }

        if (userRepository.findUserByUsername(login).isEmpty()) {
            throw new UserDoesNotExistsException("User with provided username does not exists!");
        }

        Movie movie = movieRepository.findById(movieId).get();
        User userMovie =  userRepository.findUserByUsername(login).get();

        if (movie.isSubscriptionOnly()) {
            if (userMovie.isSubscribed()) {
                movie.getUsersThatWatchedMovies().add(userMovie);  // Adding user to list in Movie class
                movie.setWatchCount(movie.getWatchCount()+1);
                userMovie.getWatchedMovies().add(movie);  // Adding movie to list in User class
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This movie is only available to subscribed users!");
            }
        } else {
            movie.getUsersThatWatchedMovies().add(userMovie);
            movie.setWatchCount(movie.getWatchCount()+1);
            userMovie.getWatchedMovies().add(movie);
        }

        movieRepository.save(movie);
        userRepository.save(userMovie);
        return ResponseEntity.ok(movie);
    }

    public ResponseEntity<String> rateMovie(long movieId, int rating ,String login) throws UserDoesNotExistsException, MovieDoesNotExistsException {
        User user = checkUser(login);
        Movie movie = checkMovie(movieId);

        if (!user.getWatchedMovies().contains(movie)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can rate only movies that you have watched!");
        }
        movie.getUsersRatingsList().add(rating);

        int sumOfRatings = 0;
        for (Integer number : movie.getUsersRatingsList()) {
            sumOfRatings += number;
        }

        double newRating = (double) sumOfRatings/movie.getUsersRatingsList().size(); // formatting value to get only 2 digits after decimal
        newRating = round(newRating,1);

        movie.setRating(newRating);
        movieRepository.save(movie);

        return ResponseEntity.ok("Movie rating added!");
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

    private Movie checkMovie(long movieId) throws MovieDoesNotExistsException {
        Optional<Movie> optionalMovie = movieRepository.
                findById(movieId);
        if (optionalMovie.isEmpty()) {
            throw new MovieDoesNotExistsException("Movie is not in our database!");
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
