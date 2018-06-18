package com.intrum.lenc.dao;

import com.intrum.lenc.config.AppConfig;
import com.intrum.lenc.config.SecurityConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, SecurityConfig.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test.properties")
public abstract class RepositoryTest {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected DebtRepository debtRepository;

    @Before
    public void setup() {
        cleanDb();
    }

    public void cleanDb() {
        debtRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
    }
}
