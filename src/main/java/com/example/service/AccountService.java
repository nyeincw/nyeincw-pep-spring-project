package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;


@Service
public class AccountService {     

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account){
        //Check if the username is not blank
        if(account.getUsername()==null || account.getUsername().isEmpty()){
            throw new IllegalArgumentException("Username cannot be blank");
        }

        //Check if password is at least 4 characters long
        if(account.getPassword()==null || account.getPassword().length()<4){
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }

        //Check if username does not already exist
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if(existingAccount.isPresent()){
            throw new DuplicateUsernameException("Username already exists");
        }
        
        return accountRepository.save(account);
    }

    public Account loginAccount(String username, String password){
        
        return accountRepository.findByUsername(username)
            .filter(account -> account.getPassword().equals(password)) 
            .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

    }

    public Account getAccountByUsername(String username){
        return accountRepository.findByUsername(username)
            .orElseThrow(()-> new RuntimeException("Account not found"));
    }




}
