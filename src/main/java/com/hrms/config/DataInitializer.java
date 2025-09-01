package com.hrms.config;

import com.hrms.entity.Department;
import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.service.DepartmentService;
import com.hrms.service.RoleService;
import com.hrms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentService departmentService;
    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(DepartmentService departmentService, 
                          RoleService roleService, 
                          UserService userService) {
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize default departments
        initializeDepartments();
        
        // Initialize default roles
        initializeRoles();
        
        // Initialize default admin user
        initializeAdminUser();
    }

    private void initializeDepartments() {
        if (departmentService.getAllDepartments().isEmpty()) {
            Department hr = new Department("Human Resources", "Manages employee relations, recruitment, and HR policies");
            Department it = new Department("Information Technology", "Handles software development, infrastructure, and technical support");
            Department finance = new Department("Finance", "Manages financial operations, accounting, and budgeting");
            Department marketing = new Department("Marketing", "Handles marketing strategies, campaigns, and brand management");
            Department operations = new Department("Operations", "Manages day-to-day business operations and processes");

            departmentService.saveDepartment(hr);
            departmentService.saveDepartment(it);
            departmentService.saveDepartment(finance);
            departmentService.saveDepartment(marketing);
            departmentService.saveDepartment(operations);
        }
    }

    private void initializeRoles() {
        if (roleService.getAllRoles().isEmpty()) {
            Role manager = new Role("Manager", "Manages team and department operations");
            Role seniorDeveloper = new Role("Senior Developer", "Leads development projects and mentors junior developers");
            Role developer = new Role("Developer", "Develops software applications and features");
            Role hrSpecialist = new Role("HR Specialist", "Handles recruitment, employee relations, and HR processes");
            Role accountant = new Role("Accountant", "Manages financial records and accounting processes");
            Role analyst = new Role("Analyst", "Analyzes data and provides insights for business decisions");
            Role coordinator = new Role("Coordinator", "Coordinates projects and administrative tasks");

            roleService.saveRole(manager);
            roleService.saveRole(seniorDeveloper);
            roleService.saveRole(developer);
            roleService.saveRole(hrSpecialist);
            roleService.saveRole(accountant);
            roleService.saveRole(analyst);
            roleService.saveRole(coordinator);
        }
    }

    private void initializeAdminUser() {
        userService.createDefaultAdmin();
    }
}
