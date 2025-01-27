package com.apiportfolio.backendportfolio.controller;

import com.apiportfolio.backendportfolio.model.Portfolio;
import com.apiportfolio.backendportfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }

    @GetMapping("/{id}")
    public Portfolio getPortfolioById(@PathVariable String id) {
        return portfolioService.getPortfolioById(id);
    }

    @PostMapping
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
        return portfolioService.createPortfolio(portfolio);
    }

    @PutMapping("/{id}")
    public Portfolio updatePortfolio(@PathVariable String id, @RequestBody Portfolio portfolio) {
        return portfolioService.updatePortfolio(id, portfolio);
    }

    @DeleteMapping("/{id}")
    public void deletePortfolio(@PathVariable String id) {
        portfolioService.deletePortfolio(id);
    }
}
