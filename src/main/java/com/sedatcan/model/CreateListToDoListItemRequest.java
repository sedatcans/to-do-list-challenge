package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateListToDoListItemRequest {
    private  ToDoListItemDto toDoListItemDto;
}
