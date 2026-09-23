package uz.vermont.backend.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import uz.vermont.backend.model.Product;
import java.util.*;
public interface ProductRepository extends JpaRepository<Product,String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from Product p where p.id in :ids")
  List<Product> findAllByIdForUpdate(@Param("ids") Collection<String> ids);
  Optional<Product> findFirstByOrderBySoldDesc();
  List<Product> findByStockLessThanEqualOrderByStockAsc(Integer stock);
}
