package service;

import dao.UserDao;
import model.User;

import javax.security.auth.login.LoginException;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserDao userDao = new UserDao();
    private final Object createUserLock = new Object();

    @Override
    public User login(String userName, String password) throws LoginException {
        Optional<User> user = userDao.findByUserName(userName);
        if(user.isPresent()){
            if(user.get().getPassword().equals(password)){
                return user.get();
            }
            throw new LoginException("Wrong password");
        }
        throw new LoginException("User not found");
    }

    @Override
    public String createUser(User user) {
        if(user.getUserName() == null) return "username missing";
        if(user.getPassword() == null) return "password missing";
        if(user.getPermission() == null) return "permission missing";
        if(user.getReadOnly() == null) return "readonly missing";
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
