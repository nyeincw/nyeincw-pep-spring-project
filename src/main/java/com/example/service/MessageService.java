package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message){

        if(message.getMessageText()==null || message.getMessageText().isEmpty()){
            throw new IllegalArgumentException("Message text cannot be blank");
        }

        if(message.getMessageText().length()>255){
            throw new IllegalArgumentException("Message text must be under 255 characters");
        }

        Integer postedAccountID = message.getPostedBy();
        Account existingAccount = accountRepository.findById(postedAccountID)
                .orElseThrow(()-> new IllegalArgumentException("The account does not exist"));

        message.setPostedBy(postedAccountID);
        return messageRepository.save(message);
        
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();        
    }

    public Message getMessagebyId(Integer id){
        return messageRepository.findById(id).orElse(null);
    }

    public int deleteMessage(Integer id){
        if(messageRepository.existsById(id)){
            messageRepository.deleteById(id);
            return 1;
        }else{
            return 0;
        }        
    }

    public int updateMessage(Integer id, Message newMessage){

        if(newMessage.getMessageText()==null || newMessage.getMessageText().isBlank()){
            throw new IllegalArgumentException("Message text cannot be blank");
        }

        if (newMessage.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text must be under 255 characters");
        }

        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            message.setMessageText(newMessage.getMessageText());
            messageRepository.save(message);
            return 1;
        }else{
            throw new IllegalArgumentException("Message with ID " + id+ " not found");
        }
    }
     
    public List<Message> getAllMessagesbyAccountId(Integer id){
        return messageRepository.findBypostedBy(id);      
    }    
     
}
