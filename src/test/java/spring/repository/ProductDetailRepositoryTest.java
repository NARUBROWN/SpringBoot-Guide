package spring.repository;

import spring.data.entity.Product;
import spring.data.entity.ProductDetail;
import spring.data.repository.ProductDetailRepository;
import spring.data.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class ProductDetailRepositoryTest {
    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void saveAndReadTest1() {
        Product product =
                Product.builder()
                        .name("스프링부트 JPA")
                        .price(5000)
                        .stock(500)
                        .build();

        productRepository.save(product);

        ProductDetail productDetail = new ProductDetail();
        productDetail.updateProductDetail("스프링부트와 JPA를 함께 볼 수 있는 책", product);

        productDetailRepository.save(productDetail);

        // 생성한 데이터 조회
        log.info("savedProduct : " + productDetailRepository.findById(
                productDetail.getId()).get().getProduct());

        log.info("savedProductDetail : " + productDetailRepository.findById(
                productDetail.getId()).get());
    }
}
