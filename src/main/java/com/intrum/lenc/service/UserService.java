package com.intrum.lenc.service;

import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public static final String EMPTY_USER = "Empty user";
    public static final String WRONG_ID = "Wrong id";
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Check is the sane user for given id username valid to userId
     * @param userName
     * @param userId
     * @return
     */
    public boolean isSame(String userName, Long userId) {
        User user = userRepository.findByName(userName);
        return user != null && user.getId().equals(userId);
    }

    /**
     * Find users by userId
     * @param userId
     * @return
     */
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Save user
     * @param user
     * @throws ResourceNotFoundException, if user is null or id is wrong
     */
    public void save(User user) {
        if (user != null) {
            if (user.getId() == null || user.getId() > 0L) {
                userRepository.save(user);
            } else {
                throw new ResourceNotFoundException(WRONG_ID);
            }
        } else {
            throw new ResourceNotFoundException(EMPTY_USER);
        }
    }

    /**
     * detele all users
     */
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
