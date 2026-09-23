package uz.vermont.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.vermont.backend.model.PaymentInfo;
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo,Integer> {}
