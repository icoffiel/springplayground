package com.nerdery.icoffiel.web.rest.jwt.controller;

import com.nerdery.icoffiel.config.AppProperties;
import com.nerdery.icoffiel.security.jwt.JwtTokenUtil;
import com.nerdery.icoffiel.web.rest.jwt.model.JwtDto;
import com.nerdery.icoffiel.web.rest.jwt.model.LoginDto;
import com.nerdery.icoffiel.web.rest.jwttoken.model.JwtToken;
import com.nerdery.icoffiel.web.rest.jwttoken.service.JwtTokenService;
import com.nerdery.icoffiel.web.rest.user.model.User;
import com.nerdery.icoffiel.web.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for JWT requests.
 */
@RestController
@RequestMapping("/rest")
public class JWTController {

    private final JwtTokenUtil tokenUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final String header;
    private final String refreshHeader;
    private final String bearerHeader;

    @Autowired
    public JWTController(JwtTokenUtil tokenUtil, AuthenticationManager authenticationManager,
                         AppProperties appProperties, JwtTokenService jwtTokenService, UserService userService) {
        this.tokenUtil = tokenUtil;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;

        this.header = appProperties.getSecurity().getAuthentication().getJwt().getHeader();
        this.refreshHeader = appProperties.getSecurity().getAuthentication().getJwt().getRefreshHeader();
        this.bearerHeader = appProperties.getSecurity().getAuthentication().getJwt().getBearerHeader();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authorize(@RequestBody LoginDto loginDto, Device device, HttpServletResponse response)
        throws AuthenticationException{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenUtil.createToken(authentication, device);
        JwtToken refreshToken = tokenUtil.createAndPersistRefreshToken(authentication, device);

        response.addHeader(header, bearerHeader + " " + token);
        response.addHeader(refreshHeader, refreshToken.getToken());
        return ResponseEntity.ok(new JwtDto(token));

    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        // Ensure that the user has supplied correct auth details.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        String token = request.getHeader(refreshHeader);

        if (tokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = tokenUtil.refreshToken(token);
            response.addHeader(header, bearerHeader + " " + refreshedToken);
            return ResponseEntity.ok(new JwtDto(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
