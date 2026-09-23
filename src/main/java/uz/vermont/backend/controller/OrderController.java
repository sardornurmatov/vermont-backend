package uz.vermont.backend.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.vermont.backend.model.*;
import uz.vermont.backend.repository.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@RestController @RequestMapping("/api/orders")
public class OrderController {
  private final OrderRepository orders; private final ProductRepository products; private final CustomerRepository customers;
  public OrderController(OrderRepository orders,ProductRepository products,CustomerRepository customers){this.orders=orders;this.products=products;this.customers=customers;}
  public record CustomerInput(@NotBlank String name,@NotBlank String phone){}
  public record ItemInput(@NotBlank String id,@Min(1) @Max(100) int qty){}
  public record OrderRequest(@Valid @NotNull CustomerInput customer,@Valid @NotEmpty List<ItemInput> items,List<String> pickupLocations,String receipt,String paidToCard){}
  public record StatusRequest(@NotBlank String status){}
  public record ItemView(String id,String name,BigDecimal price,Integer qty,String location){}
  public record OrderView(String id,long createdAt,String customerId,CustomerInput customer,List<String> pickupLocations,BigDecimal total,String status,String receipt,String paidToCard,List<ItemView> items){}
  private OrderView view(CustomerOrder o){return new OrderView(o.getId(),o.getCreatedAt().toInstant().toEpochMilli(),o.getCustomer()==null?null:o.getCustomer().getId(),new CustomerInput(o.getCustomerName(),o.getCustomerPhone()),o.getPickupLocations(),o.getTotal(),o.getStatus(),o.getReceipt(),o.getPaidToCard(),o.getItems().stream().map(i->new ItemView(i.getProductId(),i.getName(),i.getPrice(),i.getQty(),i.getLocation())).toList());}
  @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<OrderView> all(){return orders.findAllByOrderByCreatedAtDesc().stream().map(this::view).toList();}
  @GetMapping("/mine") @PreAuthorize("hasRole('CUSTOMER')") public List<OrderView> mine(Authentication auth){return orders.findByCustomerIdOrderByCreatedAtDesc(auth.getName()).stream().map(this::view).toList();}
  @PostMapping @PreAuthorize("hasRole('CUSTOMER')") @Transactional public ResponseEntity<?> create(Authentication auth,@Valid @RequestBody OrderRequest r){
    Customer customer=customers.findById(auth.getName()).orElseThrow(); Map<String,Integer> qty=new LinkedHashMap<>();r.items().forEach(i->qty.merge(i.id(),i.qty(),Integer::sum));
    List<Product> locked=products.findAllByIdForUpdate(qty.keySet());if(locked.size()!=qty.size())return ResponseEntity.badRequest().body(Map.of("error","Mahsulot topilmadi"));
    CustomerOrder order=new CustomerOrder();order.setId("ORD-"+UUID.randomUUID());order.setCustomer(customer);order.setCustomerName(r.customer().name().trim());order.setCustomerPhone(r.customer().phone().trim());order.setPickupLocations(r.pickupLocations()==null?new ArrayList<>():r.pickupLocations());order.setReceipt(r.receipt());order.setPaidToCard(r.paidToCard());
    BigDecimal total=BigDecimal.ZERO;for(Product p:locked){int count=qty.get(p.getId());if(p.getStock()<count)return ResponseEntity.status(409).body(Map.of("error",p.getName()+" omborda yetarli emas"));OrderItem item=new OrderItem();item.setOrder(order);item.setProductId(p.getId());item.setName(p.getName());item.setPrice(p.getPrice());item.setQty(count);item.setLocation(p.getLocation());order.getItems().add(item);total=total.add(p.getPrice().multiply(BigDecimal.valueOf(count)));}order.setTotal(total);orders.save(order);return ResponseEntity.status(201).body(Map.of("ok",true,"id",order.getId(),"total",total));
  }
  @PatchMapping("/{id}/status") @PreAuthorize("hasRole('ADMIN')") @Transactional public Map<String,Boolean> status(@PathVariable String id,@Valid @RequestBody StatusRequest r){
    Set<String> allowed=Set.of("tekshirilmoqda","tasdiqlandi","topshirildi","bekor qilindi");if(!allowed.contains(r.status()))throw new IllegalArgumentException("Buyurtma holati noto‘g‘ri");CustomerOrder o=orders.findById(id).orElseThrow();if(r.status().equals("tasdiqlandi")&&!o.getSoldCounted()){for(OrderItem i:o.getItems()){Product p=products.findById(i.getProductId()).orElseThrow();if(p.getStock()<i.getQty())throw new IllegalStateException("Omborda mahsulot yetarli emas");p.setStock(p.getStock()-i.getQty());p.setSold(p.getSold()+i.getQty());}o.setSoldCounted(true);}o.setStatus(r.status());return Map.of("ok",true);}
}
