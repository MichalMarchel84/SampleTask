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

        //Checking if given user exists and password is correct.
        //In real world scenario it should be evaluated if returning detailed
        //info on which part of authentication was incorrect is actually required.
        //Such information enables to verify if user name exists and poses potential security threat.
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

        //Validation. As there were no detailed instructions on data format, checking
        //only for presence of "not null" fields.
        if(user.getUserName() == null) return "username missing";
        if(user.getPassword() == null) return "password missing";
        if(user.getPermission() == null) return "permission missing";
        if(user.getReadOnly() == null) return "readonly missing";

        //As column "username" has no UNIQUE constraint and Tomcat handles each request in separate
        //thread, synchronization is implemented to make sure there will be no duplicate values
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
