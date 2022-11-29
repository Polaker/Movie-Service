package com.example.movieservice.user;

import com.example.movieservice.exception.UserAlreadyExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) throws UserAlreadyExistsException {

        Optional<User> optionalUser = userRepository.
                findUserByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        Optional<User> optionalUser2 = userRepository.
                findOneByEmailIgnoreCase(user.getEmail());
        if (optionalUser2.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        if (user.getId() == 1) {
            user.setRoles("ROLE_ADMIN");
        } else {
            user.setRoles("ROLE_USER");
        }

        userRepository.save(user);
    }

    @Transactional
    public Object showWatchedMovies(String login) throws UserDoesNotExistsException {
        Optional<User> optionalUser = userRepository.
                findUserByUsername(login);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistsException("User with provided username does not exists!");
        }
        User user = optionalUser.get();
        return user.getWatchedMovies();
    }

    @Transactional
    public Object showWatchedSeries(String login) throws UserDoesNotExistsException {
        Optional<User> optionalUser = userRepository.
                findUserByUsername(login);
        if (optionalUser.isEmpty()) {
            throw new UserDoesNotExistsException("User with provided username does not exists!");
        }
        User user = optionalUser.get();
        return user.getWatchedSeries();
    }
}
