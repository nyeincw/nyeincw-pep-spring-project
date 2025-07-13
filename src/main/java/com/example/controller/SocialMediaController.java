package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account){
        try{
            Account savedAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(savedAccount);
        }catch(DuplicateUsernameException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration is unsuccessful");
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account){
        try{
            Account found = accountService.loginAccount(account.getUsername(),account.getPassword());
            return ResponseEntity.ok(found);
        }catch(UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message){
        try {
            Message savedMessage=messageService.createMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message creation unsuccessful");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessagebyID(@PathVariable Integer message_id){
        Message message=messageService.getMessagebyId(message_id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer message_id){
        int deletedmessage=messageService.deleteMessage(message_id);
        if(deletedmessage == 1){
            return ResponseEntity.ok(1);
        }else{
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody Message new_Message){
        try{
            int updatedmessage = messageService.updateMessage(message_id, new_Message);
            return ResponseEntity.ok(updatedmessage);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }       
        
    }

    
    @GetMapping("/accounts/{accountID}/messages")
    public ResponseEntity<List<Message>> getAllMessagesbyAccountID(@PathVariable int accountID){
        List<Message> messages = messageService.getAllMessagesbyAccountId(accountID);
        return ResponseEntity.ok(messages);
    }
    
}
