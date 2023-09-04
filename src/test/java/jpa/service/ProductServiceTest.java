package jpa.service;

import jpa.data.dto.request.ProductDTO;
import jpa.data.dto.response.ProductResponseDto;
import jpa.data.entity.Product;
import jpa.data.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;

/*
    단위 테스트를 위해서는 외부 요인을 모두 배제하도록 코드를 작성해야 한다.
    이번 예제에서는 ControllerTest와 달리 @WebMvcTest 같은 어노테이션이 선언되어 있지 않다.
*/
public class ProductServiceTest {
    /*  Mockito의 mock 메서드를 통해 Mock 객체로 ProductRepository를 주입 받았다.
        이 객체를 기반으로 @BeforeEach를 통해 테스트 전에 ProductService 객체를 초기화해서 사용한다.
     */
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private ProductService productService;

    @BeforeEach
    public void setUpTeset() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getProductTest() {
        // Given에 해당 되는 밑 구문은 Product Entity를 만든다.
        Product givenProduct = new Product(123L, "펜", 1000, 1234);

        Mockito.when(productRepository.findById(123L))
                .thenReturn(Optional.of(givenProduct));

        ProductResponseDto productResponseDto = productService.getProduct(123L);

        Assertions.assertEquals(productResponseDto.getNumber(), givenProduct.getNumber());
        Assertions.assertEquals(productResponseDto.getName(), givenProduct.getName());
        Assertions.assertEquals(productResponseDto.getPrice(), givenProduct.getPrice());
        Assertions.assertEquals(productResponseDto.getStock(), givenProduct.getStock());

        verify(productRepository).findById(123L);
    }

    @Test
    void saveProduct() {
        Mockito.when(productRepository.save(any(Product.class)))
                .then(returnsFirstArg());

        ProductResponseDto productResponseDto = productService.saveProduct(new ProductDTO("펜", 1000, 1234));

        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        verify(productRepository).save(any());
    }
}
