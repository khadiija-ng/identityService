package com.disi.identyService.advice;
import java.lang.RuntimeException;



public class UserNotFoundException  extends RuntimeException{
    public UserNotFoundException(int userId){
        super("User not found with Id: " + userId);
    }

}
