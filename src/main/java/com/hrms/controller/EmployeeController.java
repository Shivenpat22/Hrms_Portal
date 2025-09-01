package com.hrms.controller;

import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.entity.Role;
import com.hrms.service.DepartmentService;
import com.hrms.service.EmployeeService;
import com.hrms.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public EmployeeController(EmployeeService employeeService, 
                            DepartmentService departmentService, 
                            RoleService roleService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listEmployees(Model model, 
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "name") String sortBy,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               @RequestParam(required = false) String search) {
        
        if (search != null && !search.trim().isEmpty()) {
            Page<Employee> employees = employeeService.searchEmployees(search, page, size);
            model.addAttribute("employees", employees);
            model.addAttribute("search", search);
        } else {
            Page<Employee> employees = employeeService.getAllEmployeesPaginated(page, size, sortBy, sortDir);
            model.addAttribute("employees", employees);
        }
        
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("roles", roleService.getAllRoles());
        return "employees/form";
    }

    @PostMapping("/new")
    public String createEmployee(@Valid @ModelAttribute Employee employee,
                                BindingResult result,
                                @RequestParam(value = "photo", required = false) MultipartFile photo,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        // Check for duplicate email
        if (employeeService.existsByEmail(employee.getEmail())) {
            result.rejectValue("email", "error.employee", "Email already exists");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        // Check for duplicate mobile
        if (employeeService.existsByMobile(employee.getMobile())) {
            result.rejectValue("mobile", "error.employee", "Mobile number already exists");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            try {
                String photoUrl = employeeService.uploadEmployeePhoto(photo);
                employee.setPhotoUrl(photoUrl);
            } catch (Exception e) {
                result.rejectValue("photoUrl", "error.employee", "Failed to upload photo: " + e.getMessage());
                model.addAttribute("departments", departmentService.getAllDepartments());
                model.addAttribute("roles", roleService.getAllRoles());
                return "employees/form";
            }
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("success", "Employee created successfully!");
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employees/view";
        } else {
            return "redirect:/employees";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        } else {
            return "redirect:/employees";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateEmployee(@PathVariable Long id,
                                @Valid @ModelAttribute Employee employee,
                                BindingResult result,
                                @RequestParam(value = "photo", required = false) MultipartFile photo,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (!existingEmployee.isPresent()) {
            return "redirect:/employees";
        }

        Employee existing = existingEmployee.get();

        // Check for duplicate email (excluding current employee)
        if (!existing.getEmail().equals(employee.getEmail()) && 
            employeeService.existsByEmail(employee.getEmail())) {
            result.rejectValue("email", "error.employee", "Email already exists");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        // Check for duplicate mobile (excluding current employee)
        if (!existing.getMobile().equals(employee.getMobile()) && 
            employeeService.existsByMobile(employee.getMobile())) {
            result.rejectValue("mobile", "error.employee", "Mobile number already exists");
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("roles", roleService.getAllRoles());
            return "employees/form";
        }

        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            try {
                String photoUrl = employeeService.uploadEmployeePhoto(photo);
                employee.setPhotoUrl(photoUrl);
            } catch (Exception e) {
                result.rejectValue("photoUrl", "error.employee", "Failed to upload photo: " + e.getMessage());
                model.addAttribute("departments", departmentService.getAllDepartments());
                model.addAttribute("roles", roleService.getAllRoles());
                return "employees/form";
            }
        } else {
            // Keep existing photo if no new photo uploaded
            employee.setPhotoUrl(existing.getPhotoUrl());
        }

        employee.setId(id);
        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("success", "Employee updated successfully!");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Employee deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found!");
        }
        return "redirect:/employees";
    }
}
