package com.sedatcan;

import com.sedatcan.repository.CustomerRepository;
import com.sedatcan.repository.ToDoListItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean({CustomerRepository.class,
        ToDoListItemRepository.class})
public class ToDoListChallengeApplicationTests {

    @Test
    public void contextLoads() {
    }

}
