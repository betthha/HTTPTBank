package monitor;

import com.example.currency.service.CurrencyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMonitorJob {
    private final CurrencyService currencyService;

    public CurrencyMonitorJob(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void monitorCurrencyRates() {
        currencyService.checkCurrencyRates();
    }
}