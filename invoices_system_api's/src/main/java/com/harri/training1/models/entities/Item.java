package com.harri.training1.models.entities;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryParameterValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String name;
    private float price;
    private int quantity;

    public Map<String, QueryParameterValue> toStructFields() {
        Map<String, QueryParameterValue> structData = new LinkedHashMap<>();
        structData.put("name", QueryParameterValue.string(this.name));
        structData.put("price", QueryParameterValue.float64(this.price));
        structData.put("quantity", QueryParameterValue.int64(this.quantity));
        return structData;
    }

    public static Item fromStructField(FieldValue fieldValue) {
        FieldValueList structFields = fieldValue.getRecordValue();
        String name = structFields.get("name").getStringValue();
        float price = (float) structFields.get("price").getDoubleValue();
        int quantity = (int) structFields.get("quantity").getLongValue();

        return new Item(name, price, quantity);
    }
}


