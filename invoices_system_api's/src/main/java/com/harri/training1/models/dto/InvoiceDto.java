package com.harri.training1.models.dto;

import com.harri.training1.models.entities.Item;
import lombok.Data;
import java.util.List;

@Data
public class InvoiceDto {
    private long id;
    private float amount;
    private String createdAt;
    private long userId;
    private List<Item> items;
}
