package com.hrms.controller;

import com.hrms.entity.Department;
import com.hrms.service.DepartmentService;
import com.hrms.service.EmployeeService;
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
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public DepartmentController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping("/new")
    public String createDepartment(@Valid @ModelAttribute Department department,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "departments/form";
        }

        // Check for duplicate department name
        if (departmentService.existsByName(department.getName())) {
            result.rejectValue("name", "error.department", "Department name already exists");
            return "departments/form";
        }

        departmentService.saveDepartment(department);
        redirectAttributes.addFlashAttribute("success", "Department created successfully!");
        return "redirect:/departments";
    }

    @GetMapping("/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            model.addAttribute("employees", employeeService.getEmployeesByDepartment(id));
            model.addAttribute("employeeCount", employeeService.getEmployeeCountByDepartment(id));
            return "departments/view";
        } else {
            return "redirect:/departments";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/form";
        } else {
            return "redirect:/departments";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateDepartment(@PathVariable Long id,
                                  @Valid @ModelAttribute Department department,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "departments/form";
        }

        Optional<Department> existingDepartment = departmentService.getDepartmentById(id);
        if (!existingDepartment.isPresent()) {
            return "redirect:/departments";
        }

        Department existing = existingDepartment.get();

        // Check for duplicate department name (excluding current department)
        if (!existing.getName().equals(department.getName()) && 
            departmentService.existsByName(department.getName())) {
            result.rejectValue("name", "error.department", "Department name already exists");
            return "departments/form";
        }

        department.setId(id);
        departmentService.saveDepartment(department);
        redirectAttributes.addFlashAttribute("success", "Department updated successfully!");
        return "redirect:/departments";
    }

    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            // Check if department has employees
            long employeeCount = employeeService.getEmployeeCountByDepartment(id);
            if (employeeCount > 0) {
                redirectAttributes.addFlashAttribute("error", 
                    "Cannot delete department. It has " + employeeCount + " employee(s) assigned to it.");
            } else {
                departmentService.deleteDepartment(id);
                redirectAttributes.addFlashAttribute("success", "Department deleted successfully!");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Department not found!");
        }
        return "redirect:/departments";
    }
}
