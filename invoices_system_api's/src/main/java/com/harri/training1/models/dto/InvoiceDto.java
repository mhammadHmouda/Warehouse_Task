package com.harri.training1.models.dto;

import com.harri.training1.models.entities.Item;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceDto {
    private Long id;
    private float amount;
    private Long userId;
    private List<Item> items;
}
