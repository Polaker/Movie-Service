package com.example.movieservice.wallet;

import com.example.movieservice.exception.UserDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @PutMapping
    public ResponseEntity<String> depositToWallet(@AuthenticationPrincipal Principal principal,
                                                  @RequestParam int moneyAmount) throws UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return walletService.depositToWallet(moneyAmount, login);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_SUBSCRIBER')")
    @GetMapping
    public ResponseEntity<Wallet> checkWalletBalance(@AuthenticationPrincipal Principal principal) throws UserDoesNotExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return walletService.checkWalletBalance(login);
    }
}
