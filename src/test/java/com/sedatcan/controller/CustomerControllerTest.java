package com.sedatcan.controller;

import com.sedatcan.model.CustomerDto;
import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;
import com.sedatcan.service.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CustomerControllerTest extends BaseControllerTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void shouldSignupThenReturnToken() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().email("sedatcan@gmail.com").name("sedatcan").password("password").surname("sonat").build();
        when(customerService.signup(signupRequest)).thenReturn(SignupResponse.builder()
                .token("I am Token").customerDto(CustomerDto.builder()
                        .email("sedatcan@gmail.com")
                        .name("sedatcan")
                        .password("password")
                        .surname("sonat")
                        .id("Customer1").build()).build());

        ResultActions resultActions = mockMvc()
                .perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)));

        resultActions.andExpect(status().isAccepted())
                .andExpect(unauthenticated())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.token", equalTo("I am Token")))
                .andExpect(jsonPath("$.customerDto.id", equalTo("Customer1")));

        verify(customerService).signup(signupRequest);

    }

}