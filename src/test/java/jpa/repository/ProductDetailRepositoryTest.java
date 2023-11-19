package jpa.repository;

import jpa.data.entity.Product;
import jpa.data.entity.ProductDetail;
import jpa.data.repository.ProductDetailRepository;
import jpa.data.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("prod1")
@Slf4j
public class ProductDetailRepositoryTest {
    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void saveAndReadTest1() {
        Product product = Product.builder()
                .name("스프링 부트 JPA")
                .price(5000)
                .stock(500)
                .build();
        productRepository.save(product);

        ProductDetail productDetail = ProductDetail.builder()
                .product(product)
                .description("스프링 부트와 JPA를 함께 볼 수 있는 책")
                .build();
        productDetailRepository.save(productDetail);

        log.info("savedProduct : " + productDetailRepository.findById(productDetail.getId()).get().getProduct());
        log.info("saedProductDetail : " + productDetailRepository.findById(productDetail.getId()).get());
    }

}
