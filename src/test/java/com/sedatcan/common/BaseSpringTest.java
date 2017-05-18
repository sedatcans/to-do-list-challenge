package com.sedatcan.common;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static org.mockito.Mockito.inOrder;

@RunWith(SpringRunner.class)
public abstract class BaseSpringTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Before
    public void before() {
    }

    @After
    public void after() {
        verifyNoMoreInteractionsWithMockedBeans();
    }
    protected InOrder inOrderVerifier() {
        return inOrder(collectMockedBeans());
    }

    private void verifyNoMoreInteractionsWithMockedBeans() {
        Stream.of(collectMockedBeans())
                .forEach(Mockito::verifyNoMoreInteractions);
    }

    private Object[] collectMockedBeans() {
        return Stream.of(applicationContext.getBeanDefinitionNames())
                .map(beanName -> applicationContext.getBean(beanName))
                .filter(bean -> bean.getClass().getName().contains("EnhancerByMockito"))
                .toArray();
    }

}
