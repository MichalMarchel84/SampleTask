package service;

import model.BlogEntry;
import model.User;

import java.util.List;

public interface BlogService {

    List<BlogEntry> getAll();
    String createEntry(BlogEntry entry, User user);
    String deleteEntry(int id, User user);
}
