package com.sedatcan.service.impl;

import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.document.JsonLongDocument;
import com.sedatcan.common.BaseServiceTest;
import com.sedatcan.entity.ToDoListItem;
import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.*;
import com.sedatcan.repository.ToDoListItemRepository;
import com.sedatcan.service.ToDoListItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ToDoListItemServiceImplTest extends BaseServiceTest {

    @Autowired
    private ToDoListItemRepository toDoListItemRepository;

    @Autowired
    private ToDoListItemService toDoListItemService;

    @Test
    public void shouldCreateItem() throws Exception {
        CreateListToDoListItemRequest createListToDoListItemRequest = CreateListToDoListItemRequest.builder()
                .toDoListItemDto(ToDoListItemDto.builder().note("note").build()).build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(CustomerDto.builder().id("id").build());
        SecurityContextHolder.setContext(securityContext);

        CouchbaseOperations couchbaseOperations = mock(CouchbaseOperations.class);
        CouchbaseBucket couchbaseBucket = mock(CouchbaseBucket.class);
        JsonLongDocument jsonLong = mock(JsonLongDocument.class);

        when(toDoListItemRepository.getCouchbaseOperations()).thenReturn(couchbaseOperations);
        when(couchbaseOperations.getCouchbaseBucket()).thenReturn(couchbaseBucket);
        when(couchbaseBucket.counter("idGeneratorForItem", 1, 0)).thenReturn(jsonLong);
        when(jsonLong.content()).thenReturn(1L);

        when(toDoListItemRepository.save(any(ToDoListItem.class))).thenReturn(ToDoListItem.builder().id("id").note("note").build());
        toDoListItemService.create(createListToDoListItemRequest);
        verify(toDoListItemRepository).save(any(ToDoListItem.class));
        verify(toDoListItemRepository).getCouchbaseOperations();
        verify(couchbaseOperations).getCouchbaseBucket();
        verify(couchbaseBucket).counter("idGeneratorForItem", 1, 0);
        verify(jsonLong).content();
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        UpdateListToDoListItemRequest updateListToDoListItemRequest = UpdateListToDoListItemRequest.builder()
                .toDoListItemDto(ToDoListItemDto.builder().customerId("custId").id("id").status(ToDoListItem.Status.DONE).note("note").build()).build();
        when(toDoListItemRepository.save(any(ToDoListItem.class))).thenReturn(ToDoListItem.builder().customerId("custId").id("id").note("note").build());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(CustomerDto.builder().id("custId").build());
        SecurityContextHolder.setContext(securityContext);

        toDoListItemService.update("id", updateListToDoListItemRequest);

        verify(toDoListItemRepository).save(any(ToDoListItem.class));
    }

    @Test(expected = ToDoListException.class)
    public void shouldUpdateItemIdNotMatch() throws Exception {
        UpdateListToDoListItemRequest updateListToDoListItemRequest = UpdateListToDoListItemRequest.builder()
                .toDoListItemDto(ToDoListItemDto.builder().customerId("custId2").id("id2").status(ToDoListItem.Status.DONE).note("note").build()).build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(CustomerDto.builder().id("custId").build());
        SecurityContextHolder.setContext(securityContext);

        toDoListItemService.update("id2", updateListToDoListItemRequest);
    }

    @Test
    public void getAllItems() throws Exception {
        ToDoListItem toDoItem = ToDoListItem.builder().customerId("custId").id("id").status(ToDoListItem.Status.UNDONE).note("note").build();
        when(toDoListItemRepository.findByCustomerIdAndStatus("custId", "UNDONE")).thenReturn(Arrays.asList(toDoItem));
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(CustomerDto.builder().id("custId").build());
        SecurityContextHolder.setContext(securityContext);

        ToDoListItemsOfCustomerResponse toDoListItemsOfCustomerResponse = toDoListItemService.getAllItems();

        assertThat(toDoListItemsOfCustomerResponse.getToDoListItems().get(0).getCustomerId(), equalTo("custId"));
        assertThat(toDoListItemsOfCustomerResponse.getToDoListItems().get(0).getNote(), equalTo("note"));

        verify(toDoListItemRepository).findByCustomerIdAndStatus("custId", "UNDONE");
    }

}