package jpa.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "provider")
public class Provider extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "provider", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @ToString.Exclude // 양방향 관계에서는 ToString 순환 참조 문제가 항상 일어날 수 있다는 점을 잊지 말하야 함
    private List<Product> productList = new ArrayList<>();


    @Builder
    public Provider (Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
