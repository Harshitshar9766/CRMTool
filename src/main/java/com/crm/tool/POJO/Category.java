package com.crm.tool.POJO;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Category.getAllCategory", query = "select c from Category c")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUTD = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Integer id;
    @Column(name = "name")
    private  String name;



}
