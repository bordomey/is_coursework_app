package com.game.track.service;

import com.game.track.dto.RoleDto;
import com.game.track.entity.Role;
import com.game.track.exception.ResourceNotFoundException;
import com.game.track.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoleDto getRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        return convertToDto(role);
    }

    private RoleDto convertToDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }
}