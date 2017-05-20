package com.sedatcan.controller;

import com.sedatcan.model.*;
import com.sedatcan.service.ToDoListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/items")
@CrossOrigin
public class ToDoListItemController {

    @Autowired
    private ToDoListItemService toDoListItemService;

    @PostMapping
    public ResponseEntity<CreateListToDoListItemResponse> create(@RequestBody @Valid CreateListToDoListItemRequest createListToDoListItemRequest) {
        toDoListItemService.create(createListToDoListItemRequest);
        return new ResponseEntity<CreateListToDoListItemResponse>(HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<UpdateListToDoListItemResponse> update(@PathVariable @NotNull String itemId, @RequestBody @Valid UpdateListToDoListItemRequest updateListToDoListItemRequest) {
        toDoListItemService.update(itemId, updateListToDoListItemRequest);
        return new ResponseEntity<UpdateListToDoListItemResponse>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ToDoListItemsOfCustomerResponse> getAll() {
        return new ResponseEntity<ToDoListItemsOfCustomerResponse>(toDoListItemService.getAllItems(), HttpStatus.OK);
    }
}
