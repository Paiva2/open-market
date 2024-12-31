package org.com.openmarket.market.application.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeycloakClaimsConfig implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final static String ROLE_PREFIX = "realm_role_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        List<String> rolesClaimed = source.getClaim("role");

        if (rolesClaimed == null || rolesClaimed.isEmpty()) return null;

        for (String role : rolesClaimed) {
            if (role.startsWith(ROLE_PREFIX)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.replace(ROLE_PREFIX, "")));
            }
        }

        return authorities;
    }
}
