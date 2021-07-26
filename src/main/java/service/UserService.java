package service;

import model.User;

public interface UserService {
    String login(String userName, String password);
    String createUser(User user);
}
