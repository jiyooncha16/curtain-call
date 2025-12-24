package com.ssafy.curtaincall;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.user.dto.User;
import com.ssafy.curtaincall.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

    	System.out.println(mapper.selectDbName());
        User user = mapper.selectUser(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("유저 없음")
            );

        return new CustomUserDetails(user);
    }
}

