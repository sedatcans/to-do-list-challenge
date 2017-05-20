package com.sedatcan.service.impl;

import com.sedatcan.entity.ToDoListItem;
import com.sedatcan.exception.ToDoListErrorCode;
import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.*;
import com.sedatcan.repository.ToDoListItemRepository;
import com.sedatcan.service.ToDoListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoListItemServiceImpl implements ToDoListItemService {
    @Autowired
    private ToDoListItemRepository toDoListItemRepository;

    @Override
    public CreateListToDoListItemResponse create(CreateListToDoListItemRequest createListToDoListItemRequest) {
            CustomerDto customer = (CustomerDto) SecurityContextHolder.getContext().getAuthentication();
        ToDoListItem toDoListItem = ToDoListItem.builder()
                .id("Item" + toDoListItemRepository.getCouchbaseOperations().getCouchbaseBucket().counter("idGeneratorForItem", 1, 0).content())
                .createdDate(new Date())
                .customerId(customer.getId())
                .note(createListToDoListItemRequest.getToDoListItemDto().getNote())
                .status(ToDoListItem.Status.UNDONE).build();
        toDoListItemRepository.save(toDoListItem);
        return new CreateListToDoListItemResponse();
    }

    @Override
    public UpdateListToDoListItemResponse update(String itemId, UpdateListToDoListItemRequest updateListToDoListItemRequest) {
        CustomerDto customer = (CustomerDto) SecurityContextHolder.getContext().getAuthentication();
        if (updateListToDoListItemRequest.getToDoListItemDto().getCustomerId() !=null && !updateListToDoListItemRequest.getToDoListItemDto().getCustomerId().equals(customer.getId())) {
            throw new ToDoListException(ToDoListErrorCode.UNAUTHORIZED_REQUEST, HttpStatus.UNAUTHORIZED);
        }
        ToDoListItem toDoListItem = ToDoListItem.builder()
                .id(itemId)
                .createdDate(updateListToDoListItemRequest.getToDoListItemDto().getCreatedDate())
                .customerId(customer.getId())
                .note(updateListToDoListItemRequest.getToDoListItemDto().getNote())
                .status(updateListToDoListItemRequest.getToDoListItemDto().getStatus())
                .build();
        toDoListItemRepository.save(toDoListItem);
        return new UpdateListToDoListItemResponse();
    }

    @Override
    public ToDoListItemsOfCustomerResponse getAllItems() {
        CustomerDto customer = (CustomerDto) SecurityContextHolder.getContext().getAuthentication();
        List<ToDoListItem> toDoListItems = toDoListItemRepository.findByCustomerIdAndStatus(customer.getId(), "UNDONE");
        List<ToDoListItemDto> toDoListItemDtos = toDoListItems.stream()
                .map(toDoListItem -> ToDoListItemDto.builder().id(toDoListItem.getId()).createdDate(toDoListItem.getCreatedDate()).customerId(toDoListItem.getCustomerId()).note(toDoListItem.getNote()).status(toDoListItem.getStatus()).build())
                .collect(Collectors.toList());
        return ToDoListItemsOfCustomerResponse.builder().toDoListItems(toDoListItemDtos).build();
    }
}
