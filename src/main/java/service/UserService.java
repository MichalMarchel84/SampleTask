package service;

import model.User;

import javax.security.auth.login.LoginException;

public interface UserService {
    User login(String userName, String password) throws LoginException;
    String createUser(User user);
}
