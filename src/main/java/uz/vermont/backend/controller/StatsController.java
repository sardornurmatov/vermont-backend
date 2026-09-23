package uz.vermont.backend.controller;
import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import uz.vermont.backend.repository.*;import java.util.*;
@RestController @RequestMapping("/api/stats") @PreAuthorize("hasRole('ADMIN')")
public class StatsController {
 private final OrderRepository orders;private final ProductRepository products;public StatsController(OrderRepository orders,ProductRepository products){this.orders=orders;this.products=products;}
 @GetMapping public Map<String,Object> get(){var all=products.findAll();long sold=all.stream().mapToLong(p->p.getSold()).sum();var top=products.findFirstByOrderBySoldDesc().<Object>map(p->Map.of("id",p.getId(),"name",p.getName(),"sold",p.getSold())).orElse(null);Map<String,Object> out=new LinkedHashMap<>();out.put("revenue",orders.revenue(List.of("tasdiqlandi","topshirildi")));out.put("totalOrders",orders.count());out.put("pending",orders.countByStatus("tekshirilmoqda"));out.put("totalSold",sold);out.put("topProduct",top);out.put("lowStock",products.findByStockLessThanEqualOrderByStockAsc(5).stream().map(p->Map.of("id",p.getId(),"name",p.getName(),"stock",p.getStock())).toList());return out;}
}
