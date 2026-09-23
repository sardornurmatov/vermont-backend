package uz.vermont.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers", indexes = @Index(name = "idx_customers_email_lower", columnList = "email", unique = true))
@Getter @Setter @NoArgsConstructor
public class Customer {
  @Id private String id;
  @Column(nullable = false, length = 100) private String name;
  @Column(nullable = false, unique = true) private String email;
  private String phone;
  @Column(name = "password_hash", nullable = false) private String passwordHash;
  @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt = OffsetDateTime.now();
}
