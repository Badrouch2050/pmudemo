package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void disableUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatut("DESACTIVE");
            userRepository.save(user);
        });
    }

    public void changeUserRole(Long id, String role) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRoles(new HashSet
            <>(Arrays.asList(role)));
            userRepository.save(user);
        });
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void addRole(Long id, String role) {
        userRepository.findById(id).ifPresent(user -> {
            user.getRoles().add(role);
            userRepository.save(user);
        });
    }

    public void removeRole(Long id, String role) {
        userRepository.findById(id).ifPresent(user -> {
            user.getRoles().remove(role);
            userRepository.save(user);
        });
    }
} 