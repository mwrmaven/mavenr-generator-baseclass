package com.maven.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.String;
import java.lang.Integer;

@Table(name = "t")
public class T {

    /**
    * id
    */
    @Column(name = "id")
    private Integer id;

    /**
    * name
    */
    @Column(name = "name")
    private String name;

    /**
    * age
    */
    @Column(name = "age")
    private Integer age;

}