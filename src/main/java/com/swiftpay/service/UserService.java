package com.swiftpay.service;

import com.swiftpay.dto.UserDto;
import com.swiftpay.exception.ResourceNotFoundException;
import com.swiftpay.mapper.UserMapper;
import com.swiftpay.model.Agency;
import com.swiftpay.model.Role;
import com.swiftpay.model.User;
import com.swiftpay.repository.AgencyRepository;
import com.swiftpay.repository.RoleRepository;
import com.swiftpay.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AgencyRepository agencyRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, AgencyRepository agencyRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.agencyRepository = agencyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        user.setRoles(roleRepository.findAllByUser(user));
        return new MyUserPrincipal(this, user);
    }

    public User getAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {

            User user = new User();
            user.setUsername("anonymous");
            user.setRoles(List.of(new Role(user, "ROLE_ANONYMOUS")));
            user.setAgency(null);

            return user;
        }

        return userRepository.findByUsername(auth.getName());
    }

    public List<UserDto> getAllUsers() {
        return  userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public void createUser(Long agencyId, String username, String password, String role) {


        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency not found"));

        String hashedPassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
        User user = new User(username, hashedPassword);
        user.setAgency(agency);

        User savedUser = userRepository.save(user);
        roleRepository.save(new Role(savedUser, String.format("ROLE_%s", role)));
    }
}