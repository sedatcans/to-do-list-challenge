package com.sedatcan.service;

import com.sedatcan.model.*;

public interface ToDoListItemService {
    CreateListToDoListItemResponse create(CreateListToDoListItemRequest createListToDoListItemRequest);

    UpdateListToDoListItemResponse update(String itemId, UpdateListToDoListItemRequest updateListToDoListItemRequest);

    ToDoListItemsOfCustomerResponse getAllItems();
}
