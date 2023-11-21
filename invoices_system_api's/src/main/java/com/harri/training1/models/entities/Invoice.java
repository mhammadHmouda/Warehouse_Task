package com.harri.training1.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private Long id;
    private String createdAt = LocalDate.now().toString();
    private float amount;
    private List<Item> items;
    private File file;
    private Long userId;
}
