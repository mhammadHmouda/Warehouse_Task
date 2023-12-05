package com.harri.training1.mapper;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.harri.training1.annotations.FieldMapping;
import com.harri.training1.annotations.AccessibleBy;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Component
public class GenericMapper<T> {

    private static final String DEFAULT_STRING_VALUE = "-";
    private static final int DEFAULT_INT_VALUE = 0;
    private static final float DEFAULT_FLOAT_VALUE = 0.0f;

    public T mapRow(@NonNull FieldValueList row, @NonNull Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                AccessibleBy annotation = field.getAnnotation(AccessibleBy.class);

                if (annotation != null && !hasAuthority(annotation.value())) {
                    setFieldToDefaultValue(instance, field);
                } else {
                    String fieldMapping = field.getAnnotation(FieldMapping.class).value();
                    fieldMapping = convertToCamelCase(fieldMapping);
                    Object value = convertToFieldType(row.get(fieldMapping), field);
                    field.set(instance, value);
                }
            }

            return instance;
        } catch (Exception e) {
            System.out.println("Error mapping BigQuery result to object: " + e.getMessage());
            return null;
        }
    }

    private void setFieldToDefaultValue(T instance, Field field) throws IllegalAccessException {
        Class<?> fieldType = field.getType();

        switch (fieldType.getSimpleName()) {
            case "String" -> field.set(instance, DEFAULT_STRING_VALUE);
            case "long", "Integer" -> field.set(instance, DEFAULT_INT_VALUE);
            case "double", "float" -> field.set(instance, DEFAULT_FLOAT_VALUE);
            default -> field.set(instance, null);
        }
    }

    private Object convertToFieldType(FieldValue fieldValue, Field field) {
        if (fieldValue.isNull()) {
            return null;
        }

        Class<?> fieldType = field.getType();
        String name = fieldType.getSimpleName();

        return switch (name) {
            case "String" -> fieldValue.getStringValue();
            case "long" -> fieldValue.getLongValue();
            case "int" -> (int) fieldValue.getLongValue();
            case "double" -> fieldValue.getDoubleValue();
            case "float" -> (float) fieldValue.getDoubleValue();
            case "boolean" -> fieldValue.getBooleanValue();
            default -> throw new IllegalArgumentException("Unsupported fieldType: " + name);
        };
    }

    private boolean hasAuthority(String... requiredAuthorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) return false;

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            for (String requiredAuthority : requiredAuthorities) {
                if (grantedAuthority.getAuthority().equalsIgnoreCase(requiredAuthority)) return true;
            }
        }

        return false;
    }

    public String convertToCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (c == '_') capitalizeNext = true;
            else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }
}
