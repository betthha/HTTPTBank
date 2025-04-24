import com.example.currency.entity.CurrencyEntity;
import model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<java.util.Currency, Long> {
    List<Currency> findAll();
}