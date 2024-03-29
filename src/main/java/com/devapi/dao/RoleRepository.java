package com.devapi.dao;

import com.devapi.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

//@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role, UUID> {
        @Query("SELECT p FROM Role p WHERE p.role = ?1")
        List<Role> findByRoleName(String roleName);

        @Query("SELECT count(p) > 0 FROM Role p WHERE p.role = ?1")
        boolean existsByName(String roleName);

        List<Role> findAllByRoleIn(List<String> roleNames);

}
