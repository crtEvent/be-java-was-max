package application.db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import application.model.User;

public class UserDatabase {
    private static final Map<String, User> users = Maps.newLinkedHashMap();

    static {
        User user = new User("admin", "1234", "관리자","email.@emai.com");
        users.put(user.getUserId(), user);

        for(int i = 1; i <= 100; i++) {
            String formattedNumber = String.format("%03d", i);
            users.put("user"+formattedNumber
                , new User("user"+formattedNumber
                    , "1234"
                    , "이름"+formattedNumber
                    , "email"+formattedNumber+"@email.com"));
        }
    }

    private UserDatabase() {}

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
