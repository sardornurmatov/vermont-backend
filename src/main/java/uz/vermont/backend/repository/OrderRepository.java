package uz.vermont.backend.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import uz.vermont.backend.model.CustomerOrder;
import java.math.BigDecimal;
import java.util.List;
public interface OrderRepository extends JpaRepository<CustomerOrder,String> {
  List<CustomerOrder> findAllByOrderByCreatedAtDesc();
  List<CustomerOrder> findByCustomerIdOrderByCreatedAtDesc(String customerId);
  long countByStatus(String status);
  @Query("select coalesce(sum(o.total),0) from CustomerOrder o where o.status in :statuses")
  BigDecimal revenue(@Param("statuses") List<String> statuses);
}
