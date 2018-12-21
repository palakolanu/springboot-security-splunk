package com.roja.rest.dao;

import org.springframework.stereotype.Repository;

import com.roja.rest.model.Employee;
import com.roja.rest.model.Employees;

@Repository
public class EmployeeDAO 
{
    private static Employees list = new Employees();
    
    static 
    {
        list.getEmployeeList().add(new Employee(1, "Roja", "R", "roja@gmail.com"));
        list.getEmployeeList().add(new Employee(2, "Alex", "Hello", "abc@gmail.com"));
        list.getEmployeeList().add(new Employee(3, "David", "Kam", "123@gmail.com"));
    }
    
    public Employees getAllEmployees() 
    {
        return list;
    }
    
    public void addEmployee(Employee employee) {
        list.getEmployeeList().add(employee);
    }
}
