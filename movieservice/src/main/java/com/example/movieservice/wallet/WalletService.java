package com.example.movieservice.wallet;

import com.example.movieservice.exception.UserDoesNotExistsException;
import com.example.movieservice.user.User;
import com.example.movieservice.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public ResponseEntity<String> depositToWallet(int moneyAmount, String login) throws UserDoesNotExistsException {
        User user = checkUser(login);
        if (moneyAmount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot deposit less than 1$");
        }
        user.getWallet().setMoneyAmmount(moneyAmount);
        userRepository.save(user);

        return ResponseEntity.ok("Wallet updated");
    }

    public ResponseEntity<Wallet> checkWalletBalance(String login) throws UserDoesNotExistsException {
        User user = checkUser(login);
        return ResponseEntity.ok(user.getWallet());
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
