package com.tutorail.apidemo.Springboot.database;

import com.tutorail.apidemo.Springboot.models.Product;
import com.tutorail.apidemo.Springboot.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //anotation này chứa những bean method liên quan đến database như khởi tạo database hay biến môi trường
public class Database {

    //logger: tương tự như sout nhưng có thể in ra được các thuộc tính cụ thể như info, warning, error, ...
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    //hàm init sẽ insert một vài bản ghi fake trong db, nếu chưa có bảng thì sẽ tự động tạo bảng
    //gọi là Code first
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        //tạo ra một đối tượng để thực thi interface CommandLineRuner
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product productA = new Product("Macbook M1", 2020, 2000.0, "");
//                Product productB = new Product("Iphone 13", 2021, 1400.0, "");
//
//                //hiển thị ra console những thông tin, báo lỗi, ... để thuận tiện quá trình đọc log và fix bug
//                //hàm save
//                logger.info("Insert data " + productRepository.save(productA));
//                logger.info("Insert data " + productRepository.save(productB));
            }
        };
    }
}
