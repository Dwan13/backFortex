package com.springboot.backend.felipe.types.types_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.felipe.types.types_backend.entities.Role;
import com.springboot.backend.felipe.types.types_backend.entities.User;
import com.springboot.backend.felipe.types.types_backend.models.IUser;
import com.springboot.backend.felipe.types.types_backend.models.UserRequest;
import com.springboot.backend.felipe.types.types_backend.repositories.RoleRepository;
import com.springboot.backend.felipe.types.types_backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3; // Intentos permitidos antes del bloqueo

    @Autowired
    private UserRepository repository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setLastname(user.getLastname());
            userDb.setName(user.getName());
            userDb.setUsername(user.getUsername());

            userDb.setRoles(getRoles(user));
            return Optional.of(repository.save(userDb));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
   
    @Override
    @Transactional
    public void increaseFailedAttempts(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int attempts = user.getFailed_attempts() != null ? user.getFailed_attempts() : 0;
            attempts++;

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setLocked_account(true);
            }

            user.setFailed_attempts(attempts);
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public void resetFailedAttempts(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFailed_attempts(0);
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public void unlockAccount(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFailed_attempts(0);
            user.setLocked_account(false);
            repository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccountLocked(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        return userOptional.map(User::getLocked_account).orElse(false);
    }

}
