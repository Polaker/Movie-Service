package com.example.movieservice.user;

import com.example.movieservice.exception.SubscriptionAlreadyBoughtException;
import com.example.movieservice.exception.UserAlreadyExistsException;
import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.wallet.Wallet;
import com.example.movieservice.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
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
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }
        Wallet wallet = new Wallet();
        walletRepository.save(wallet);

        user.setWallet(wallet);
        userRepository.save(user);
    }

    @Transactional
    public Object showWatchedMovies(String login) throws UserDoesNotExistsException {
        User user = checkUser(login);
        return user.getWatchedMovies();
    }

    @Transactional
    public Object showWatchedSeries(String login) throws UserDoesNotExistsException {
        User user = checkUser(login);
        return user.getWatchedSeries();
    }

    @Transactional
    public ResponseEntity<String> buySubscription(String login) throws UserDoesNotExistsException, SubscriptionAlreadyBoughtException {
        User user = checkUser(login);

        if (user.getWallet().getMoneyAmmount() < 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your balance is too low! Subscription costs 100. Your account balance is " + user.getWallet().getMoneyAmmount());
        }
        if (user.isSubscribed()) {
            throw new SubscriptionAlreadyBoughtException("Your subscription is already active!");
        }

        user.getWallet().setMoneyAmmount(user.getWallet().getMoneyAmmount()-100);
        user.setSubscribed(true);
        user.setRole("ROLE_SUBSCRIBER");
        userRepository.save(user);
        return ResponseEntity.ok("Your lifetime subscription has been bought!");
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
}
