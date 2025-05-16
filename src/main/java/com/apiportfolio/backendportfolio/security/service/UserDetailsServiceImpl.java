package com.apiportfolio.backendportfolio.security.service;

import com.apiportfolio.backendportfolio.security.model.Portfolio;
import com.apiportfolio.backendportfolio.security.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Portfolio Portfolio = portfolioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(Portfolio.getEmail(), Portfolio.getPassword(), new ArrayList<>());
    }
}