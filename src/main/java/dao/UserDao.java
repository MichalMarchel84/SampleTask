package dao;

import model.User;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {

    public Optional<User> findByUserName(String userName) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                User user = new User();
                user.setUserId(res.getInt("userid"));
                user.setUserName(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setPermission(res.getString("permission"));
                user.setReadOnly(res.getString("readonly"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public int createUser(User user) {
        String sql = "INSERT INTO user VALUES (null, ?, ?, ?, ?)";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getPermission());
            stmt.setString(4, user.getReadOnly());
            return stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
}
