package com.tutorail.apidemo.Springboot.controllers;

import com.tutorail.apidemo.Springboot.models.Product;
import com.tutorail.apidemo.Springboot.models.ResponObject;
import com.tutorail.apidemo.Springboot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {

    //DI = Dependency Injection (tương tự với @Inject trong servlet)
    @Autowired
    private ProductRepository repository;

    @GetMapping("/get")  // method Get dùng để lấy data từ server
    //localhost:8080/apt/v1/Products/get
    List<Product> getAllProduct() {
        return repository.findAll(); //đối tượng được tạo ra bên trên và findAll để lấy ra tất cả data
    }

    //lấy đối tượng có id trùng với Path variable
    @GetMapping("/{id}")  //localhost:8080/apt/v1/Products/id
    //trả về một object gồm có status, message và data
    ResponseEntity<ResponObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);  //Optional = có thể một object hoặc null
        if(foundProduct.isPresent()) {
            //nếu tìm thấy id (product có tồn tại) thì trả về product đó + status + message
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponObject("Ok", "Query product successfully", foundProduct)
            );
        } else {
            //nếu không tìm thấy thì trả về data rỗng kèm theo message và status Fail
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponObject("Fail", "Cannot find product with id = " + id, "")
            );
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponObject> insertProduct(@RequestBody Product newProduct) {
        //Nếu muốn hai product không được trùng tên
        //tạo một hàm findByProductName và đưa kết quả vào một list.
        // Nếu list > 0 thì không thực thi insert nữa (Not implemented)
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponObject("Fail", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject("Ok", "Insert product successfully", repository.save(newProduct))
        );
    }

    //upsert = update hoặc insert: Nếu đối tượng cần update chưa tồn tại thì sẽ insert luôn
    @PutMapping("/{id}")
    ResponseEntity<ResponObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updated = repository.findById(id)
                //nếu tìm thấy đối tượng cần update thì update chính đối tượng đó
                .map(product -> {  //biểu thức lamda
                    product.setProductName(newProduct.getProductName());
                    product.setPrice(newProduct.getPrice());
                    product.setYear(newProduct.getYear());
                    product.setUrl(newProduct.getUrl());

                    return repository.save(product);
                //nếu không tìm thấy thì tạo mới
                }).orElseGet(()-> {
                    newProduct.setId(id); //id ở đây vẫn tự tăng
                    return repository.save(newProduct);
                });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponObject("Ok", "Update product successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);  //kiểm tra xem id cần xóa đã tồn tại hay chưa
        if(exists) {
            //nếu có tồn tại thì thực hện xóa
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponObject("Ok", "Delete product successfully", "")
            );
        }
        //nếu không thì trả về thông báo
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponObject("Fail", "Cannot find product to delete", "")
        );


        //chưa biết cách fix lỗi delete product thì bị trống id
    }

}
