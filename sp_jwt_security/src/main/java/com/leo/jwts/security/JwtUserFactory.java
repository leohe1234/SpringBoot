package com.leo.jwts.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.leo.jwts.user.User;

public final class JwtUserFactory {

        private JwtUserFactory() {
        }

        public static JwtUser create(User user) {
            return new JwtUser(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    mapToGrantedAuthorities(user.getRole()),
                    user.getUpdateDate()
            );
        }

        private static List<GrantedAuthority> mapToGrantedAuthorities(String roles) {
        	
        	String[] rs = roles.split(";");
        	List<String> authorities = Arrays.asList(rs);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }


