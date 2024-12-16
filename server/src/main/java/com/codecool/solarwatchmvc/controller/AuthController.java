package com.codecool.solarwatchmvc.controller;

import com.codecool.solarwatchmvc.DTO.AuthResponseDTO;
import com.codecool.solarwatchmvc.DTO.LoginDTO;
import com.codecool.solarwatchmvc.DTO.RegisterDTO;
import com.codecool.solarwatchmvc.model.Role;
import com.codecool.solarwatchmvc.model.UserEntity;
import com.codecool.solarwatchmvc.repository.RoleRepository;
import com.codecool.solarwatchmvc.repository.UserRepository;
import com.codecool.solarwatchmvc.security.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JWTUtils jwtUtils) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        logger.info(String.valueOf(registerDTO));
        if (userRepository.existsByUsername(registerDTO.username())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.username());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));

        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Set.of(role));

        userRepository.save(user);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                registerDTO.username(),
                                registerDTO.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new AuthResponseDTO("User registration success"), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        logger.info(String.valueOf(loginDTO));
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.username(),
                                loginDTO.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token, "User login successfully"), HttpStatus.OK);
    }
}
