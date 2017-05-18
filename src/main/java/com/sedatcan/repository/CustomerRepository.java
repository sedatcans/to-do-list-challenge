package com.sedatcan.repository;

import com.sedatcan.entity.Customer;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface CustomerRepository extends CouchbaseRepository<Customer, String> {
    Customer findByEmail(String email);
}
