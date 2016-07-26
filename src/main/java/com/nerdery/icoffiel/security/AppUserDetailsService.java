package com.nerdery.icoffiel.security;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.nerdery.icoffiel.web.rest.user.model.Authority;
import com.nerdery.icoffiel.web.rest.user.model.User;
import com.nerdery.icoffiel.web.rest.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Custom User Details service used to retrieve Users.
 */
@Component("userDetailsService") // To Inject into WebSecurityConfig we need to use interfaces with the @Transactional
public class AppUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loadedUser = userRepository.findByUsername(username);

        if (null == loadedUser) {
            throw new UsernameNotFoundException("User '" + username + "' was not found");
        } else {
            List<GrantedAuthority> grantedAuthorities = transformAuthorities(loadedUser);

           return new org.springframework.security.core.userdetails.User(
                   loadedUser.getUsername(),
                   loadedUser.getPassword(),
                   grantedAuthorities);
        }
    }

    private List<GrantedAuthority> transformAuthorities(User loadedUser) {
        return Lists.transform(loadedUser.getAuthorities(), new Function<Authority, GrantedAuthority>() {
            @Override
            public GrantedAuthority apply(Authority authority) {
                return new SimpleGrantedAuthority(authority.getAuthority());
            }
        });
    }
}
