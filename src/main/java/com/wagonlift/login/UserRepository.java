package com.wagonlift.login;


public interface UserRepository {
 
    public User findByEmail(String email);
}
