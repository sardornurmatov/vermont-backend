package uz.vermont.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.vermont.backend.model.Customer;
import java.util.Optional;
public interface CustomerRepository extends JpaRepository<Customer,String> {
  Optional<Customer> findByEmailIgnoreCase(String email);
  boolean existsByEmailIgnoreCase(String email);
}
