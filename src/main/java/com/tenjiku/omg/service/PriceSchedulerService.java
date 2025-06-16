package com.tenjiku.omg.service;

import com.tenjiku.omg.entity.Price;
import com.tenjiku.omg.entity.Product;
import com.tenjiku.omg.repositroy.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class PriceSchedulerService{

    private final ProductRepo productRepository;

    @Scheduled(fixedRate = 20 * 60 * 1000) // Every 10 minutes
    public void updateCurrentPrices() {
        System.out.println("Running scheduled price update...");

        List<Product> products = productRepository.findAllWithPrices();

        for (Product product : products) {
            List<Price> prices = product.getPrices();
            if (prices == null || prices.isEmpty()) continue;

            Price latest = prices.stream()
                    .filter(price -> !price.getStartDate().isAfter(LocalDateTime.now()))
                    .max(Comparator.comparing(Price::getStartDate))
                    .orElse(null);

            if (latest != null && !latest.equals(product.getCurrentPrice())) {
                product.setCurrentPrice(latest);
                latest.setOwnerOfCurrentPrice(product);
                System.out.println("Updated current price for product {} to {}"+" "+ product.getId()+" "+ latest.getAmount());
            }
        }

        productRepository.saveAll(products);
        System.out.println("Price update completed.");
    }
}

