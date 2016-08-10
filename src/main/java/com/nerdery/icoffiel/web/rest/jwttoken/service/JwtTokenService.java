package com.nerdery.icoffiel.web.rest.jwttoken.service;

import com.nerdery.icoffiel.web.rest.jwttoken.model.JwtToken;
import com.nerdery.icoffiel.web.rest.jwttoken.repository.JwtTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link JwtToken} objects.
 */
@Slf4j
@Service
public class JwtTokenService {
    private final JwtTokenRepository tokenRepository;

    @Autowired
    public JwtTokenService(JwtTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public JwtToken createToken(JwtToken token) {
        log.debug("Creating Refresh token");
        return tokenRepository.save(token);
    }

    public JwtToken findOne(Long id) {
        log.debug(String.format("Loading token with ID of %d", id));
        return tokenRepository.findOne(id);
    }

    public JwtToken findOneByToken(String token) {
        log.debug((String.format("Loading token %s", token)));
        return tokenRepository.findOneByToken(token);
    }
}
