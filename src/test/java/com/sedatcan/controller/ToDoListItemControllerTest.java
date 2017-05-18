package com.sedatcan.controller;

import com.sedatcan.entity.ToDoListItem;
import com.sedatcan.model.*;
import com.sedatcan.service.ToDoListItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ToDoListItemControllerTest extends BaseControllerTest {

    @Autowired
    private ToDoListItemService toDoListItemService;

    @Test
    public void shouldCreateNewToDolistItem() throws Exception {
        CreateListToDoListItemRequest createRequest = CreateListToDoListItemRequest.builder()
                .toDoListItemDto(ToDoListItemDto.builder().note("FirstNote test").build()).build();
        when(toDoListItemService.create(createRequest)).thenReturn(new CreateListToDoListItemResponse());

        ResultActions resultActions = mockMvc()
                .perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createRequest)).with(authentication()));

        resultActions.andExpect(status().isCreated())
                .andExpect(authenticated());

        verify(toDoListItemService).create(createRequest);

    }

    @Test
    public void shouldUpdateTodoListItem() throws Exception {
        UpdateListToDoListItemRequest updateRequest = UpdateListToDoListItemRequest.builder()
                .toDoListItemDto(ToDoListItemDto.builder().note("FirstNote test").status(ToDoListItem.Status.DONE).build()).build();
        when(toDoListItemService.update("item1", updateRequest)).thenReturn(new UpdateListToDoListItemResponse());

        ResultActions resultActions = mockMvc()
                .perform(put("/items/item1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateRequest)).with(authentication()));

        resultActions.andExpect(status().isOk())
                .andExpect(authenticated());

        verify(toDoListItemService).update("item1", updateRequest);
    }

    @Test
    public void getAll() throws Exception {
        when(toDoListItemService.getAllItems()).thenReturn(ToDoListItemsOfCustomerResponse.builder()
                .toDoListItem(ToDoListItemDto.builder()
                        .note("FirstNote test")
                        .status(ToDoListItem.Status.UNDONE).build()).build());

        ResultActions resultActions = mockMvc()
                .perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON).with(authentication()));

        resultActions.andExpect(status().isOk())
                .andExpect(authenticated());

        verify(toDoListItemService).getAllItems();
    }

}