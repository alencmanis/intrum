package com.intrum.lenc.service;

import com.intrum.lenc.dao.UserRepository;
import com.intrum.lenc.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepository;

    @Test
    public void saveEmptyUser() {
        try {
            userService.save(null);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(UserService.EMPTY_USER));
        }
    }

    @Test
    public void saveWrongId() {
        try {
            User user = new User(-5L, "name", "password");
            userService.save(user);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().startsWith(UserService.WRONG_ID));
        }
    }

    @Test
    public void isSame() {
        User user = new User(1L, "name", "pass");
        when(userRepository.findByName(Mockito.anyString())).thenReturn(user);
        boolean result = userService.isSame(user.getName(), user.getId());
        Assert.assertTrue(result);
    }
}
