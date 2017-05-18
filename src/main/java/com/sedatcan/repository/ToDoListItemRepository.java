package com.sedatcan.repository;

import com.sedatcan.entity.ToDoListItem;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

public interface ToDoListItemRepository extends CouchbaseRepository<ToDoListItem, String> {
    List<ToDoListItem> findByCustomerIdAndStatus(String customerId, String status);
}
