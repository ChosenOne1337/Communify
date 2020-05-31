package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        var user = userRepository.findByName(userName);
        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("User with name '%s' does not exist", userName)
            );
        }

        return new CustomUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                Collections.emptyList()
        );

    }
}
