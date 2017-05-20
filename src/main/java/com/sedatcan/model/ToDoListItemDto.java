package com.sedatcan.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sedatcan.entity.ToDoListItem;
import com.sedatcan.util.JsonSimpleDateDeserializer;
import com.sedatcan.util.JsonSimpleDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@AllArgsConstructor
@Data
public class ToDoListItemDto {

    private String id;

    private String customerId;

    private ToDoListItem.Status status;

    private String note;

    @JsonDeserialize(using = JsonSimpleDateDeserializer.class)
    @JsonSerialize(using = JsonSimpleDateSerializer.class)
    private Date createdDate;
}
