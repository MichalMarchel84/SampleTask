package service;

import dao.UserDao;
import model.User;

import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserDao userDao = new UserDao();
    private final Object createUserLock = new Object();

    @Override
    public String login(String userName, String password) {
        Optional<User> user = userDao.findByUserName(userName);
        if(user.isPresent()){
            if(user.get().getPassword().equals(password)){
                return null;
            }
            return "Wrong password";
        }
        return "User not found";
    }

    @Override
    public String createUser(User user) {
        synchronized (createUserLock) {
            Optional<User> uo = userDao.findByUserName(user.getUserName());
            if (uo.isEmpty()) {
                if(userDao.createUser(user) > 0){
                    return null;
                }
                return "Failed to create user";
            }
        }
        return "User already exists";
    }
}
