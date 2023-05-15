package application.db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import application.model.User;

public class Database {
    private static final Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
