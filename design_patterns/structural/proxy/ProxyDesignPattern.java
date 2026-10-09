package week_3;

import java.util.HashMap;
import java.util.Map;


interface UserRepository {
    String getUserById(int id);
}

class RealUserRepository implements UserRepository {

    public RealUserRepository() {
        System.out.println("Connecting to database...");
    }

    @Override
    public String getUserById(int id) {
        System.out.println("Fetching user " + id + " from DB");
        return "User-" + id;
    }
}


class UserRepositoryProxy implements UserRepository {

    private RealUserRepository realRepo;
    private Map<Integer, String> cache = new HashMap<>();
    private String role;

    public UserRepositoryProxy(String role) {
        this.role = role;
    }

    @Override
    public String getUserById(int id) {

        // 🔐 Access control
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied to DB");
        }

        // ⚡ Cache check
        if (cache.containsKey(id)) {
            System.out.println("Returning user " + id + " from cache");
            return cache.get(id);
        }

        // 💤 Lazy DB connection
        if (realRepo == null) {
            realRepo = new RealUserRepository();
        }

        String user = realRepo.getUserById(id);
        cache.put(id, user);
        return user;
    }
}

public class ProxyDesignPattern {
    public static void main(String[] args) {

        UserRepository repo =
                new UserRepositoryProxy("ADMIN");

        System.out.println(repo.getUserById(1));
        System.out.println(repo.getUserById(1)); // cache hit
    }
}