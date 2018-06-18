package com.intrum.lenc.dao;

import com.intrum.lenc.model.User;
import org.junit.Assert;
import org.junit.Test;

public class UserRepositoryTest extends RepositoryTest {
    @Test
    public void testFindByName() {
        User user = new User("userName", "userPassword");
        userRepository.save(user);

        User found = userRepository.findByName(user.getName());
        Assert.assertEquals(user, found);
    }
}
