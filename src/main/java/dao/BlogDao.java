package dao;

import model.BlogEntry;
import utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogDao {

    public List<BlogEntry> getAll() {
        List<BlogEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM blog";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                BlogEntry entry = new BlogEntry();
                entry.setId(res.getInt("id"));
                entry.setText(res.getString("text"));
                entry.setUserId(res.getInt("userid"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public int createEntry(BlogEntry entry) {
        String sql = "INSERT INTO blog VALUES (null, ?, ?)";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entry.getText());
            stmt.setInt(2, entry.getUserId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteEntry(int id){
        String sql = "DELETE FROM blog WHERE id = ?";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
