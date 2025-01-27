package com.apiportfolio.backendportfolio.service;

import com.apiportfolio.backendportfolio.model.Portfolio;
import com.apiportfolio.backendportfolio.repository.PortfolioRepository;
import com.apiportfolio.backendportfolio.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Portfolio getPortfolioById(String id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio with ID: " + id + " not found."));
    }

    public Portfolio createPortfolio(Portfolio portfolio) {
        if (portfolio.getFullName() == null || portfolio.getFullName().isEmpty()) {
            throw new ValidationException("The name field cannot be empty.");
        }
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(String id, Portfolio portfolio) {
        if (!portfolioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot update. Portfolio with ID: " + id + " not found.");
        }
        portfolio.setId(id);
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(String id) {
        if (!portfolioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Portfolio with ID: " + id + " not found.");
        }
        portfolioRepository.deleteById(id);
    }
}
