package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class ToDoListItemsOfCustomerResponse {
    private @Singular
    List<ToDoListItemDto> toDoListItems;
}
