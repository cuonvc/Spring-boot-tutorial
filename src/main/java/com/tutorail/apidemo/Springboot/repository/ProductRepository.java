package com.tutorail.apidemo.Springboot.repository;

import com.tutorail.apidemo.Springboot.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//JpaRepository<T, primaryKeyType(id)>
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductName(String prName);
}
