package com.hrms.controller;

import com.hrms.service.DepartmentService;
import com.hrms.service.EmployeeService;
import com.hrms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public DashboardController(EmployeeService employeeService, 
                             DepartmentService departmentService, 
                             RoleService roleService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalEmployees", employeeService.getTotalEmployees());
        model.addAttribute("totalDepartments", departmentService.getTotalDepartments());
        model.addAttribute("totalRoles", roleService.getTotalRoles());
        return "dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalEmployees", employeeService.getTotalEmployees());
        model.addAttribute("totalDepartments", departmentService.getTotalDepartments());
        model.addAttribute("totalRoles", roleService.getTotalRoles());
        return "admin/dashboard";
    }
}
