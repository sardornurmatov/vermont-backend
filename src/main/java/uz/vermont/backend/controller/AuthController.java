package uz.vermont.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.vermont.backend.model.Customer;
import uz.vermont.backend.repository.CustomerRepository;
import uz.vermont.backend.security.JwtService;
import java.time.Duration;
import java.util.*;

@RestController
public class AuthController {
  private final CustomerRepository customers; private final PasswordEncoder encoder; private final JwtService jwt;
  private final String adminPassword;
  public AuthController(CustomerRepository customers, PasswordEncoder encoder, JwtService jwt,
    @Value("${app.admin-password}") String adminPassword) {
    this.customers=customers; this.encoder=encoder; this.jwt=jwt; this.adminPassword=adminPassword;
  }
  public record RegisterRequest(@NotBlank @Size(min=2,max=100) String name, @NotBlank @Email String email,
    @NotBlank @Size(min=6,max=128) String password) {}
  public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
  public record AdminLoginRequest(@NotBlank String password) {}
  public record CustomerView(String id,String name,String email,String phone) {}
  private Map<String,Object> response(Customer c) {
    return Map.of("token",jwt.create(c.getId(),"CUSTOMER",Duration.ofDays(7)),
      "customer",new CustomerView(c.getId(),c.getName(),c.getEmail(),c.getPhone()));
  }
  @PostMapping("/api/customers/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
    String email=request.email().trim().toLowerCase();
    if(customers.existsByEmailIgnoreCase(email)) return ResponseEntity.status(409).body(Map.of("error","Bu email allaqachon ro‘yxatdan o‘tgan"));
    Customer c=new Customer(); c.setId("customer-"+UUID.randomUUID()); c.setName(request.name().trim()); c.setEmail(email); c.setPasswordHash(encoder.encode(request.password()));
    return ResponseEntity.status(201).body(response(customers.save(c)));
  }
  @PostMapping("/api/customers/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    var c=customers.findByEmailIgnoreCase(request.email().trim()).orElse(null);
    if(c==null||!encoder.matches(request.password(),c.getPasswordHash())) return ResponseEntity.status(401).body(Map.of("error","Email yoki parol noto‘g‘ri"));
    return ResponseEntity.ok(response(c));
  }
  @PostMapping("/api/auth/admin/login")
  public ResponseEntity<?> admin(@Valid @RequestBody AdminLoginRequest request) {
    if(!Objects.equals(request.password(),adminPassword)) return ResponseEntity.status(401).body(Map.of("error","Parol noto‘g‘ri"));
    return ResponseEntity.ok(Map.of("token",jwt.create("admin","ADMIN",Duration.ofHours(12))));
  }
}
