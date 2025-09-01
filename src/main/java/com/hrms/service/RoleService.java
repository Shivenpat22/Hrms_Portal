package com.hrms.service;

import com.hrms.entity.Role;
import com.hrms.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByTitle(String title) {
        return roleRepository.findByTitle(title);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public boolean existsByTitle(String title) {
        return roleRepository.existsByTitle(title);
    }

    public long getTotalRoles() {
        return roleRepository.countAllRoles();
    }

    public List<Role> searchRolesByTitle(String title) {
        return roleRepository.findByTitleContainingIgnoreCase(title);
    }
}
