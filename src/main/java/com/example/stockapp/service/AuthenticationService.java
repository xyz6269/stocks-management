package com.example.stockapp.service;

import com.example.stockapp.config.ApplicationConfig;
import com.example.stockapp.config.JwtService;
import com.example.stockapp.dto.AuthenticationRequest;
import com.example.stockapp.dto.AuthenticationResponse;
import com.example.stockapp.dto.RegisterRequest;
import com.example.stockapp.entity.Role;
import com.example.stockapp.entity.User;
import com.example.stockapp.repository.RoleRepository;
import com.example.stockapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;

    public AuthenticationResponse register(RegisterRequest request) {
        log.info(request.toString());
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("user already exists");
        }

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .roles(Collections.singletonList(roleRepository.findByName("USER").get()))
                .organisation(request.getOrganisation())
                .password(applicationConfig.passwordEncoder().encode(request.getPassword()))
                .build();
        log.info(newUser.getAuthorities().toString());
        log.info("SHIT "+newUser.getAuthorities().toString());
        userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(newUser);

        return AuthenticationResponse.builder().token(jwtToken).authorisations(newUser.getRoles().stream().map(role -> role.toString()).collect(Collectors.toList())).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info(request.toString());
        applicationConfig.authenticationProvider().authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        log.info(request.toString());
        log.info(userRepository.findUserByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException()).getFirstName());
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(()-> new IllegalArgumentException("not found"));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).authorisations(user.getRoles().stream().map(role -> role.toString()).collect(Collectors.toList())).build();
    }

    @Transactional()
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName()+" hcuibhjknabhv hibjociyv hjo");
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userRepository.findUserByEmail(authentication.getName()).orElseThrow(()-> new RuntimeException("No user with this email"));
        }else{
            throw new RuntimeException("the jwt is incomplete");
        }
    }

}
