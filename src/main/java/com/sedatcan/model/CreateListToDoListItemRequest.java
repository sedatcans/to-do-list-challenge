package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateListToDoListItemRequest {
    private ToDoListItemDto toDoListItemDto;
}
