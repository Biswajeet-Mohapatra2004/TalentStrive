package com.Biswajeet.JobBoardApplication.Config;

import com.Biswajeet.JobBoardApplication.Repository.EmployerRepository;
import com.Biswajeet.JobBoardApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, AuthenticationProvider> providers = new HashMap<>();

    public CustomAuthenticationManager(AuthenticationProvider userAuthProvider, AuthenticationProvider employerAuthProvider) {
        providers.put("user", userAuthProvider);
        providers.put("employer", employerAuthProvider);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String providerType = determineProviderType(username);

        AuthenticationProvider provider = providers.get(providerType);
        System.out.println(provider);
        if (provider == null) {
            throw new AuthenticationException("No AuthenticationProvider found for type: " + providerType) {};
        }

        return provider.authenticate(authentication);
    }

    private String determineProviderType(String username) {
        if(employerRepository.existsByUsername(username)){
            System.out.println("Provider type: Employer");
            return "employer";
        }
        else if(userRepository.existsByUsername(username)){
            System.out.println("Provider type: User");
            return "user";
        }
        else{
            throw new UsernameNotFoundException("User not found "+ username);
        }
    }
}
