package service;

import dao.BlogDao;
import model.BlogEntry;
import model.User;

import java.util.List;

public class BlogServiceImpl implements BlogService{

    private final BlogDao blogDao = new BlogDao();

    @Override
    public List<BlogEntry> getAll() {
        return blogDao.getAll();
    }

    @Override
    public String createEntry(BlogEntry entry, User user) {
        if(user == null) return "Not logged in";
        if(entry.getText() == null) return "text missing";
        if(user.getReadOnly().equalsIgnoreCase("yes")) return "Read only user - creating not allowed";
        entry.setUserId(user.getUserId());
        if(blogDao.createEntry(entry) == 0) return "Failed to create entry";
        return null;
    }

    @Override
    public String deleteEntry(int id, User user) {
        if(user == null) return "Not logged in";
        if(user.getReadOnly().equalsIgnoreCase("yes")) return "Read only user - deleting not allowed";
        if(blogDao.deleteEntry(id) == 0) return "Failed to delete entry";
        return null;
    }
}
