package jpa.repository;

import jpa.data.entity.Category;
import jpa.data.entity.Product;
import jpa.data.repository.CategoryRepository;
import jpa.data.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("prod1")
@Slf4j
public class CategoryRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void relationshipTest() {
        // 테스트 데이터 생성
        Product product = Product.builder()
                .name("펜")
                .price(2000)
                .stock(100)
                .build();

        productRepository.save(product);

        Category category = Category.builder()
                .code("S1")
                .name("도서")
                .product(product)
                .build();

        categoryRepository.save(category);

        // 테스트 코드
        List<Product> products = categoryRepository.findById(1L).get().getProducts();

        for(Product foundProduct: products) {
            log.info(foundProduct.getName());
        }
    }
}
