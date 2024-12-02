package com.swiftpay.repository;

import com.swiftpay.model.Role;
import com.swiftpay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findAllByUser(User user);
}
