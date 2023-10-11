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

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/*
    단위 테스트를 위해서는 외부 요인을 모두 배제하도록 코드를 작성해야 한다.
    이번 예제에서는 ControllerTest와 달리 @WebMvcTest 같은 어노테이션이 선언되어 있지 않다.
*/
public class ProductServiceTest {
    /*
        Mockito의 mock 메서드를 통해 Mock 객체로 ProductRepository를 주입 받았다.
        이 객체를 기반으로 @BeforeEach를 통해 테스트 전에 ProductService 객체를 초기화해서 사용한다.

        @MockBean을 통해서도 주입 받을 수 있다.
        @MockBean과 @Mockito의 차이점
        @MockBean을 사용하는 방식은 스프링에 Mock 객체를 등록해서 주입 받는 형식이며
        Mockito.mock()을 사용하는 방식은 스프링 빈에 등록하지 않고 직접 객체를 초기화 해서 사용하는 방식이다.
        테스트 속도에 큰 차이는 없지만, 스프링을 사용하지 않는 Mock 객체를 직접 생성하는 방식이 더 빠르게 작동한다.

       스프링 객체를 주입 받기 위해 클래스에 @ExtendWith(SpringExtension.class)을 사용해
       JUnit5의 테스트에서 스프링 테스트 컨텍스트를 사용하도록 설정합니다.
       그리고 @Autowired 어노테이션으로 주입 받는 ProductService를 주입 받기 위해 클래스에 @Import 어노테이션을 통해 사용합니다
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
        Product givenProduct = Product.builder()
                .number(123L)
                .name("pen")
                .price(1000)
                .stock(1234)
                .build();

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
