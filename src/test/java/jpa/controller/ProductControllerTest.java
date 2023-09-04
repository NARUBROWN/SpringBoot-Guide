package jpa.controller;

import jpa.data.dto.response.ProductResponseDto;
import jpa.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
* @WebMvcTest()
* 웹에서 사용되는 요청과 응답에 대한 테스트를 수행할 수 있습니다. 대상 클래스만 로드해 테스트를 수행하며,
* 만약 대상 클래스를 추가하지 않으면 @Controller, @RestController, @ControllerAdvice등의 컨트롤러 관련 빈 객체가 모두 로드됩니다.
* @SpringBootTest보다 가볍게 테스트하기 위해서 사용됩니다.
*
* @WebMvcTest()를 이용한 테스트는 슬라이스 테스트라고 부릅니다. 슬라이스 테스트는 단위 테스트와 통합 테스트의 중간 개념으로 이해하면 됩니다.
* 레이어드 아키텍처를 기준으로 각 레이어별로 나누어 테스트를 진행한다는 의미입니다.
* 단위 테스트를 수행하기 위해서는 모든 외부 요인을 차단하고 테스트를 진행해야 하지만 컨트롤러는 개념상 웹과 맞닿은 레이어로서 외부 요인을 차단하고
* 테스트하면 의미가 없기 때무에 슬라이스 테스트를 진행하는 경우가 많습니다.
*
* 요약 : 컨트롤러는 외부 요인을 차단하고 테스트하면 의미가 없기 때문에 슬라이스 테스트를 진행한다.
* */
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /*
     @MockBean
     실제 빈 객체가 아닌 가짜 객체를 생성해서 주입하는 역할을 수행합니다. @MockBean이 선언된 객체는
     실제 개체가 아니기 때문에 실제 행위를 수행하지 않습니다. 그렇기 때문에 해당 객체는 개발자가 Mockito의 given()메서드를 통해
     정의해야 합니다.

     ProductController가 가지고 있던 ProductService 객체에 Mock 객체를 주입했습니다.
     Mock 객체에는 테스트 과정에서 맡을 동작을 정의해야 합니다.
     */
    @MockBean
    ProductService productService;

    /**
     * @Test
     * 테스트 코드가 포함돼 있다고 선언하는 어노테이션이며, JUnit Jupiter에서는 이 어노테이션을 감지해서 테스트 계획에 포함시킵니다.
     */
    @Test
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception {
        given(productService.getProduct(123L)).willReturn(
                new ProductResponseDto(123L, "pen", 5000, 2000));
        String productId = "123";

        mockMvc.perform(
                get("/product?number=" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());
        verify(productService).getProduct(123L);
    }

}
