package spring.controller;

import com.google.gson.Gson;
import spring.data.dto.response.ProductResponseDto;
import spring.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

     Mockito에서 제공하는 given() 메서드를 통해 이 객체에서 어떤 메서드가 호출되고 어떤 파라미터를 주입 받는지 가정한 후
     WillReturn() 메서드를 통해 어떤 결과를 리턴할 것인지 정의하는 구조로 코드를 작성합니다.
     */

    @MockBean
    ProductService productService;

    /**
     * @Test
     * 테스트 코드가 포함돼 있다고 선언하는 어노테이션이며, JUnit Jupiter에서는 이 어노테이션을 감지해서 테스트 계획에 포함시킵니다.
     */
    @Test
    @WithMockUser()
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception {
        given(productService.getProduct(123L)).willReturn(
                ProductResponseDto.builder()
                        .number(123L)
                        .name("pen")
                        .price(5000)
                        .stock(2000)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
        String productId = "123";
        /*
            perform() 메서드를 이용하면 서버로 URL 요청을 보내는 것 처럼 통신 테스트 코드를 작성해서
            컨트롤러를 테스트할 수 있습니다. perform() 메서드는 MockMvcRequestBuilders에서 제공하는 HTTP 메서드로 URL을 정의해서 사용합니다.
            이 메서드는 MockHttpServletRequestBuilders는 HTTP에 매핑되는 메서드를 제공합니다.
            이 메서드는 MockHttpServletRequestBuilder 객체를 리턴하며, HTTP 요청 정보를 설정할 수 있게 됩니다.

            perform() 메서드의 결과값으로 ResultActions 객체가 리턴되는데, 예제의 39~44번 줄과 같이 andExpect() 메서드를 사용해 결과값
            검증을 수행할 수 있습니다. andExpect() 메서드에서는 ResultMatcher를 활용하는데 이를 위해 MockMvcResultMatchers 클래스에 정의돼 있는 메서드를 활용해
            생성할 수 있습니다.

            요청과 응답 전체 내용을 확인하려면 andDo() 메서드를 사용합니다.
            MockMvc의 코드는 모두 합쳐져 있어 구분하기는 애매하지만 전체적인 'When-Then'의 구조를 갖추고 있음을 확인할 수 있습니다.

            마지막으로 verify() 메서드는 지정된 메서드가 실행됐는지 검증하는 역할입니다.
            일반적으로 given()에 정의된 동작과 대응합니다.

        */
        mockMvc.perform(
                get("/product?number=" + productId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());
        verify(productService).getProduct(123L);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    @DisplayName("Product 데이터 생성 테스트")
    void createProductTest() throws Exception {
        // Mock 객체에서 특정 메서드가 실행되는 경우 실제 Return을 줄 수 없기 때문에 아래와 같이
        // 가정 사항을 만들어 줍니다.
        given(productService.saveProduct(any()))
                .willReturn(
                        ProductResponseDto.builder()
                        .number(123L)
                        .name("pen")
                        .price(5000)
                        .stock(2000)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

                ProductResponseDto productResponseDto =
                        ProductResponseDto.builder()
                        .number(123L)
                        .name("pen")
                        .price(5000)
                        .stock(2000)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Gson gson = new Gson();
                String content = gson.toJson(productResponseDto);

                mockMvc.perform(
                        post("/product")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.number").exists())
                        .andExpect(jsonPath("$.name").exists())
                        .andExpect(jsonPath("$.price").exists())
                        .andExpect(jsonPath("$.stock").exists())
                        .andDo(print());

                verify(productService).saveProduct(any());
    }

    @Test
    @WithMockUser()
    @DisplayName("Product 데이터 수정 테스트")
    void changeProductTest() throws Exception {
        given(productService.changeProductName(1L, "changed"))
                .willReturn(
                        ProductResponseDto.builder()
                                .number(1L)
                                .name("pen")
                                .price(5000)
                                .stock(2000)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                );

                ProductResponseDto productResponseDto =
                        ProductResponseDto.builder()
                        .number(1L)
                        .name("pen")
                        .price(5000)
                        .stock(2000)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                Gson gson = new Gson();
                String content = gson.toJson(productResponseDto);

                mockMvc.perform(
                        put("/product?number=1&name=changed")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.number").exists())
                        .andExpect(jsonPath("$.name").exists())
                        .andExpect(jsonPath("$.price").exists())
                        .andExpect(jsonPath("$.stock").exists())
                        .andDo(print());

                verify(productService).changeProductName(1L, "changed");
    }
}
