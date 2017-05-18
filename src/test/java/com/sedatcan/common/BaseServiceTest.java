package com.sedatcan.common;

import com.sedatcan.repository.CustomerRepository;
import com.sedatcan.repository.ToDoListItemRepository;
import com.sedatcan.security.TokenAuthenticationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@MockBean({
        BCryptPasswordEncoder.class,
        CustomerRepository.class,
        ToDoListItemRepository.class,
        TokenAuthenticationService.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class BaseServiceTest extends BaseSpringTest {
}
