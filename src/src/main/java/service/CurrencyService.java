package service;import org.springframework.stereotype.Service;
import model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CurrencyService {

    private final List<Currency> currencies = new ArrayList<>();

    public List<Currency> getAllCurrencies() {
        return new ArrayList<>(currencies);
    }

    public Currency addCurrency(Currency currency) {
        if (currency.getId() != null && isCurrencyExist(currency.getId())) {
            return currency;
        }

        currency.setId(generateUniqueId());
        currencies.add(currency);
        return currency;
    }

    public Currency getCurrencyById(String id) {
        Optional<Currency> currency = currencies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        return currency.orElseThrow(() -> new RuntimeException("Валюта с ID " + id + " не найдена"));
    }

    public Currency updateCurrency(String id, Currency newCurrencyData) {
        Currency currency = getCurrencyById(id);
        updateCurrencyDetails(currency, newCurrencyData);
        return currency;
    }

    public void deleteCurrency(String id) {
        Currency currency = getCurrencyById(id);
        currencies.remove(currency);
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    private boolean isCurrencyExist(String id) {
        return currencies.stream().anyMatch(c -> c.getId().equals(id));
    }

    private void updateCurrencyDetails(Currency existingCurrency, Currency updatedData) {
        existingCurrency.setName(updatedData.getName());
        existingCurrency.setBaseCurrency(updatedData.getBaseCurrency());
        existingCurrency.setPriceChangeRange(updatedData.getPriceChangeRange());
        existingCurrency.setDescription(updatedData.getDescription());
    }
}