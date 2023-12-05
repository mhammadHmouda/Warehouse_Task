package com.harri.training1.models.entities;

import com.harri.training1.annotations.FieldMapping;
import com.harri.training1.annotations.AccessibleBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @FieldMapping("id")
    private long id;

    @FieldMapping("created_at")
    @AccessibleBy({"SUPERUSER", "AUDITOR"})
    private String createdAt = LocalDate.now().toString();

    @FieldMapping("amount")
    @AccessibleBy("SUPERUSER")
    private float amount;

    @FieldMapping("item_name")
    private String itemName;

    @FieldMapping("item_price")
    private float itemPrice;

    @FieldMapping("item_quantity")
    private int itemQuantity;

    @FieldMapping("user_id")
    private long userId;
}
