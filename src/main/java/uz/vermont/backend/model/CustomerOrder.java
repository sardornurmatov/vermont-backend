package uz.vermont.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class CustomerOrder {
  @Id private String id;
  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "customer_id") private Customer customer;
  @Column(name = "customer_name", nullable = false) private String customerName;
  @Column(name = "customer_phone", nullable = false) private String customerPhone;
  @ElementCollection @CollectionTable(name = "order_pickup_locations", joinColumns = @JoinColumn(name = "order_id"))
  @Column(name = "location") private List<String> pickupLocations = new ArrayList<>();
  @Column(nullable = false, precision = 12, scale = 2) private BigDecimal total = BigDecimal.ZERO;
  @Column(nullable = false) private String status = "tekshirilmoqda";
  @Column(columnDefinition = "TEXT") private String receipt;
  @Column(name = "paid_to_card") private String paidToCard;
  @Column(name = "sold_counted", nullable = false) private Boolean soldCounted = false;
  @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt = OffsetDateTime.now();
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();
}
