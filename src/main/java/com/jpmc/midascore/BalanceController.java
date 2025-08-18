package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam Long userId) {
        return userRepository.findById(userId)
                .map(user -> new Balance(user.getBalance()))
                .orElse(new Balance(0f));
    }
}