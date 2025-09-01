package com.hrms.controller;

import com.hrms.entity.Role;
import com.hrms.service.EmployeeService;
import com.hrms.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final EmployeeService employeeService;

    public RoleController(RoleService roleService, EmployeeService employeeService) {
        this.roleService = roleService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "roles/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form";
    }

    @PostMapping("/new")
    public String createRole(@Valid @ModelAttribute Role role,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "roles/form";
        }

        // Check for duplicate role title
        if (roleService.existsByTitle(role.getTitle())) {
            result.rejectValue("title", "error.role", "Role title already exists");
            return "roles/form";
        }

        roleService.saveRole(role);
        redirectAttributes.addFlashAttribute("success", "Role created successfully!");
        return "redirect:/roles";
    }

    @GetMapping("/{id}")
    public String viewRole(@PathVariable Long id, Model model) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            model.addAttribute("employees", employeeService.getEmployeesByRole(id));
            return "roles/view";
        } else {
            return "redirect:/roles";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            return "roles/form";
        } else {
            return "redirect:/roles";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateRole(@PathVariable Long id,
                            @Valid @ModelAttribute Role role,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "roles/form";
        }

        Optional<Role> existingRole = roleService.getRoleById(id);
        if (!existingRole.isPresent()) {
            return "redirect:/roles";
        }

        Role existing = existingRole.get();

        // Check for duplicate role title (excluding current role)
        if (!existing.getTitle().equals(role.getTitle()) && 
            roleService.existsByTitle(role.getTitle())) {
            result.rejectValue("title", "error.role", "Role title already exists");
            return "roles/form";
        }

        role.setId(id);
        roleService.saveRole(role);
        redirectAttributes.addFlashAttribute("success", "Role updated successfully!");
        return "redirect:/roles";
    }

    @PostMapping("/{id}/delete")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Role> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            // Check if role has employees
            List<com.hrms.entity.Employee> employees = employeeService.getEmployeesByRole(id);
            if (!employees.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Cannot delete role. It has " + employees.size() + " employee(s) assigned to it.");
            } else {
                roleService.deleteRole(id);
                redirectAttributes.addFlashAttribute("success", "Role deleted successfully!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Role not found!");
        }
        return "redirect:/roles";
    }
}
