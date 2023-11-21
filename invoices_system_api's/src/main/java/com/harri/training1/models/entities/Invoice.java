package com.harri.training1.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private Long id;
    private String createdAt = LocalDate.now().toString();
    private float amount;
    private Item item;
    private File file;
    private Long userId;
}
