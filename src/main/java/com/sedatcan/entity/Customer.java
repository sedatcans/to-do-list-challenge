package com.sedatcan.entity;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class Customer {

    @Id
    @Field
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String password;

    @Field
    private String name;

    @Field
    private String surname;

    @Field
    private String email;

    @Field
    @CreatedDate
    private Date createdDate;
}
