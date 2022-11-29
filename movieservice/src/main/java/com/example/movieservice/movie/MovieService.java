package com.example.movieservice.movie;

import com.example.movieservice.exception.MovieDoesNotExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


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

        movie.getUsersThatWatchedMovies().add(userMovie);                  // Adding user to list in Movie class
        movie.setWatchCount(movie.getWatchCount()+1);

        userMovie.getWatchedMovies().add(movie);                    // Adding movie to list in User class

        movieRepository.save(movie);
        userRepository.save(userMovie);
        return ResponseEntity.ok(movie);
    }

}
