package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateListToDoListItemRequest {
    private ToDoListItemDto toDoListItemDto;
}
