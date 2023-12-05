package com.harri.training1.models.entities;

import lombok.Data;

@Data
public class File {
    private String path;
    private String name;
    private String type;
    private long size;
}
