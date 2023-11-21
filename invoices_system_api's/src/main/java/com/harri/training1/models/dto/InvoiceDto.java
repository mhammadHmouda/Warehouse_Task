package com.harri.training1.models.dto;

import com.harri.training1.models.entities.Item;
import lombok.Data;

@Data
public class InvoiceDto {
    private Long id;
    private float amount;
    private Long userId;
    private Item item;
}
