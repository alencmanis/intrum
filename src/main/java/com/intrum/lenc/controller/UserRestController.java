package com.intrum.lenc.controller;

import com.intrum.lenc.model.User;
import com.intrum.lenc.service.ResourceNotFoundException;
import com.intrum.lenc.service.UnautorizedException;
import com.intrum.lenc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController extends AbstractController {
    @Autowired
    private UserService userService;

    /**
     * Fetch user by userId, if user is logged on
     *
     * @param userId
     * @return
     */
    @GetMapping("{userId}")
    public User get(@PathVariable Long userId
            , HttpServletRequest httpServletRequest) {
        String principal = getPrincipal(httpServletRequest);
        if (userService.isSame(principal, userId)) {
            Optional<User> optional = userService.findById(userId);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new ResourceNotFoundException("User not found: " + userId);
            }
        } else {
            throw new UnautorizedException();
        }
    }

    /**
     * save user
     *
     * @param user
     * @param response status should beHttpStatus.CREATED, if user created
     */
    @PostMapping()
    public void post(@RequestBody User user, HttpServletResponse response) {
        userService.save(user);
        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", "/users/" + user.getId());
    }
}
