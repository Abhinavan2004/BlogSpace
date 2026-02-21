package com.example.demo.security;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class BlogUserDetailService implements UserDetailsService {

    private final Repository_User repository_User;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Entity_User user = repository_User.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with email : " + email)
                );

        return new BlogUserDetails(user);
    }
}