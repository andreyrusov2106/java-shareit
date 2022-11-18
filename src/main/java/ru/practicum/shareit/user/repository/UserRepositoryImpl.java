package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static long currentId;
    private static final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        currentId++;
        user.setId(currentId);
        users.put(currentId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean removeUser(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public Boolean contains(Long id) {
        return users.containsKey(id);
    }

    @Override
    public Boolean checkEmail(User user) {
        return users.values()
                .stream()
                .anyMatch(u -> u.getEmail().contains(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
    }
}
