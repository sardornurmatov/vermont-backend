package uz.vermont.backend.controller;
import jakarta.validation.Valid;import jakarta.validation.constraints.NotBlank;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import uz.vermont.backend.model.PaymentInfo;import uz.vermont.backend.repository.PaymentInfoRepository;import java.util.Map;
@RestController @RequestMapping("/api/payment-info")
public class PaymentInfoController {
 private final PaymentInfoRepository repo; public PaymentInfoController(PaymentInfoRepository repo){this.repo=repo;}
 public record Request(@NotBlank String cardNumber,@NotBlank String cardHolder,@NotBlank String bank){}
 @GetMapping public Map<String,String> get(){PaymentInfo p=repo.findById(1).orElseThrow();return Map.of("cardNumber",p.getCardNumber(),"cardHolder",p.getCardHolder(),"bank",p.getBank());}
 @PutMapping @PreAuthorize("hasRole('ADMIN')") public Map<String,Boolean> put(@Valid @RequestBody Request r){PaymentInfo p=repo.findById(1).orElseGet(PaymentInfo::new);p.setCardNumber(r.cardNumber());p.setCardHolder(r.cardHolder());p.setBank(r.bank());repo.save(p);return Map.of("ok",true);}
}
