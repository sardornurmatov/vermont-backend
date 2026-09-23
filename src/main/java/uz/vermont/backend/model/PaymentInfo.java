package uz.vermont.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_info")
@Getter @Setter @NoArgsConstructor
public class PaymentInfo {
  @Id private Integer id = 1;
  @Column(name = "card_number", nullable = false) private String cardNumber;
  @Column(name = "card_holder", nullable = false) private String cardHolder;
  @Column(nullable = false) private String bank;
}
