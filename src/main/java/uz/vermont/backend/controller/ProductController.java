package uz.vermont.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.vermont.backend.model.Product;
import uz.vermont.backend.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.*;

@RestController @RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository products;
  public ProductController(ProductRepository products){this.products=products;}
  public record ProductRequest(String id,@NotBlank String cat,@NotBlank String name,String desc,@NotNull @PositiveOrZero BigDecimal price,
    @NotNull @PositiveOrZero Integer stock,String location,String image,BigDecimal rating,Integer reviews,Integer sold){}
  public record ProductView(String id,String cat,String name,String desc,BigDecimal price,Integer stock,String location,BigDecimal rating,Integer reviews,Integer sold,String image,Boolean isCustom){}
  private ProductView view(Product p){return new ProductView(p.getId(),p.getCat(),p.getName(),p.getDescription(),p.getPrice(),p.getStock(),p.getLocation(),p.getRating(),p.getReviews(),p.getSold(),p.getImage(),p.getCustom());}
  @GetMapping public List<ProductView> list(){return products.findAll().stream().map(this::view).toList();}
  @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ProductView> create(@Valid @RequestBody ProductRequest r){
    Product p=new Product(); p.setId(r.id()==null||r.id().isBlank()?"product-"+UUID.randomUUID():r.id()); apply(p,r); p.setCustom(true);
    return ResponseEntity.status(201).body(view(products.save(p)));
  }
  @PatchMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ProductView update(@PathVariable String id,@RequestBody ProductRequest r){
    Product p=products.findById(id).orElseThrow(NoSuchElementException::new); applyPartial(p,r); return view(products.save(p));
  }
  @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<?> delete(@PathVariable String id){
    Product p=products.findById(id).orElseThrow(NoSuchElementException::new); if(!Boolean.TRUE.equals(p.getCustom())) return ResponseEntity.badRequest().body(Map.of("error","Bu mahsulotni o‘chirib bo‘lmaydi")); products.delete(p); return ResponseEntity.ok(Map.of("ok",true));
  }
  private void apply(Product p,ProductRequest r){p.setCat(r.cat());p.setName(r.name());p.setDescription(Objects.requireNonNullElse(r.desc(),""));p.setPrice(r.price());p.setStock(r.stock());p.setLocation(Objects.requireNonNullElse(r.location(),""));p.setImage(r.image());if(r.rating()!=null)p.setRating(r.rating());if(r.reviews()!=null)p.setReviews(r.reviews());if(r.sold()!=null)p.setSold(r.sold());}
  private void applyPartial(Product p,ProductRequest r){if(r.cat()!=null)p.setCat(r.cat());if(r.name()!=null)p.setName(r.name());if(r.desc()!=null)p.setDescription(r.desc());if(r.price()!=null)p.setPrice(r.price());if(r.stock()!=null)p.setStock(r.stock());if(r.location()!=null)p.setLocation(r.location());if(r.image()!=null)p.setImage(r.image());if(r.sold()!=null)p.setSold(r.sold());}
}
