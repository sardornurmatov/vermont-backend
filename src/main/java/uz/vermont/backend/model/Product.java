package uz.vermont.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product {
  @Id private String id;
  @Column(nullable = false) private String cat;
  @Column(nullable = false) private String name;
  @Column(nullable = false) private String description = "";
  @Column(nullable = false, precision = 12, scale = 2) private BigDecimal price = BigDecimal.ZERO;
  @Column(nullable = false) private Integer stock = 0;
  @Column(nullable = false) private String location = "";
  @Column(nullable = false, precision = 3, scale = 2) private BigDecimal rating = BigDecimal.valueOf(5);
  @Column(nullable = false) private Integer reviews = 0;
  @Column(nullable = false) private Integer sold = 0;
  @Column(columnDefinition = "TEXT") private String image;
  @Column(name = "is_custom", nullable = false) private Boolean custom = false;
  @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt = OffsetDateTime.now();
}
