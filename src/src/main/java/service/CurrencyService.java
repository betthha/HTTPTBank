package service;

import model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CurrencyService {
    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);
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

    public boolean checkCurrencyRateChange(String currencyId, double currentRate, double previousRate) {
        Currency currency = getCurrencyById(currencyId);
        double threshold = parsePercentage(currency.getPriceChangeRange());
        double actualChange = ((currentRate - previousRate) / previousRate) * 100;

        return Math.abs(actualChange) >= Math.abs(threshold);
    }

    public String getNotificationMessage(String currencyId, double currentRate, double previousRate) {
        Currency currency = getCurrencyById(currencyId);
        double changePercentage = ((currentRate - previousRate) / previousRate) * 100;

        if (currency.getDescription() != null && !currency.getDescription().isEmpty()) {
            return currency.getDescription();
        }

        return String.format("%s %.2f%% (было: %.4f, стало: %.4f)",
                changePercentage > 0 ? "Рост курса" : "Падение курса",
                Math.abs(changePercentage),
                previousRate,
                currentRate);
    }

    public Currency getCurrencyByCode(String code) {
        return currencies.stream()
                .filter(c -> c.getBaseCurrency().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Валюта с кодом " + code + " не найдена"));
    }

    public boolean existsByCurrencyCode(String code) {
        return currencies.stream()
                .anyMatch(c -> c.getBaseCurrency().equalsIgnoreCase(code));
    }

    public void checkAllCurrencyRates(Map<String, Double> currentRates, Map<String, Double> previousRates) {
        currencies.forEach(currency -> {
            String code = currency.getBaseCurrency();
            if (currentRates.containsKey(code) && previousRates.containsKey(code)) {
                boolean isSignificantChange = checkCurrencyRateChange(
                        currency.getId(),
                        currentRates.get(code),
                        previousRates.get(code));

                if (isSignificantChange) {
                    String message = getNotificationMessage(
                            currency.getId(),
                            currentRates.get(code),
                            previousRates.get(code));
                    log.warn("Обнаружено изменение курса: {}", message);
                    System.out.println(message);
                }
            }
        });
    }

    private double parsePercentage(String percentageStr) {
        try {
            return Double.parseDouble(percentageStr.replace("%", "").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат процентного изменения: " + percentageStr);
        }
    }
}