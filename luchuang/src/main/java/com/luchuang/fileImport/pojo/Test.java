package com.luchuang.fileImport.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "test")
public class Test {

    @Id
    private int id;

    @Column(name = "name")
    private String name;
}
