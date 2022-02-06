package com.tutorail.apidemo.Springboot.models;

import org.hibernate.annotations.Columns;

import javax.persistence.*;

//POJO = Plain Object Java Object
@Entity //anotation này giúp Database có thể hiểu đây là một thực thể (table)
public class Product {
    //define id field is primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  //auto increment trong database

    //similar 2 anotation bellow
//    @SequenceGenerator(
//            name = "product_sequence",
//            sequenceName = "product_sequence",
//            allocationSize = 1 //increment by 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "product_sequence"
//    )

    private Long id;

    //validate = constrain in sql
    @Column(nullable = false, unique = true, length = 255) // not null, not duplicate, length = 255 (varchar,...)
    private String productName;
    private int year;
    private Double price;
    private String url;

    public Product() {
    }

    public Product(String productName, int year, Double price, String url) {
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
