package jpa.repository;

import jpa.data.entity.Product;
import jpa.data.entity.Provider;
import jpa.data.repository.ProductRepository;
import jpa.data.repository.ProviderRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("prod1")
@Slf4j
public class ProviderRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProviderRepository providerRepository;

    @Test
    void relationshipTest1() {
        // 테스트용 데이터를 생성한다.
        Provider provider = Provider.builder()
                .name("OO물산")
                .build();

        providerRepository.save(provider);

        Product product = Product.builder()
                .name("가위")
                .price(5000)
                .stock(500)
                .build();

        productRepository.save(product);

        // 테스트 수행
        log.info("product : " +  productRepository.findById(1L).orElseThrow(RuntimeException::new));
        log.info("provider : " + productRepository.findById(1L).orElseThrow(RuntimeException::new));
    }

    @Test
    void relationshipTest2() {
        // 테스트 데이터 생성
        Provider provider = Provider.builder()
                .name("OO상사")
                .build();

        providerRepository.save(provider);

        Product product1 = Product.builder()
                .name("펜")
                .price(2000)
                .stock(100)
                .provider(provider)
                .build();

        Product product2 = Product.builder()
                .name("가방")
                .price(2000)
                .stock(200)
                .provider(provider)
                .build();

        Product product3 = Product.builder()
                .name("노트")
                .price(3000)
                .stock(1000)
                .provider(provider)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> products = providerRepository.findById(provider.getId()).get().getProductList();

        for(Product product :  products) {
            log.info(product.getName());
        }
    }

    @Test
    void cascadeTest() {
        Provider provider = Provider.builder()
                .name("새로운 공급업체")
                .build();

        Product product1 = Product.builder()
                .name("상품1")
                .stock(1000)
                .price(1000)
                .provider(provider)
                .build();

        Product product2 = Product.builder()
                .name("상품2")
                .stock(500)
                .price(1500)
                .provider(provider)
                .build();

        Product product3 = Product.builder()
                .name("상품3")
                .stock(750)
                .price(500)
                .provider(provider)
                .build();

        provider.getProductList().addAll(Lists.newArrayList(product1, product2, product3));

        providerRepository.save(provider);
    }
}
