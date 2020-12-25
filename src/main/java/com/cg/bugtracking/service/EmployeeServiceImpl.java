package com.cg.bugtracking.service;

import static com.cg.bugtracking.util.AppConstant.EMPLOYEE_NOT_FOUND_CONST;
import static com.cg.bugtracking.util.AppConstant.OPERATION_FAILED_CONST;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.bugtracking.entity.Employee;
import com.cg.bugtracking.exceptions.LoginOperationException;
import com.cg.bugtracking.exceptions.OperationFailedException;
import com.cg.bugtracking.exceptions.ResourceNotFoundException;
import static com.cg.bugtracking.util.AppConstant.*;
import com.cg.bugtracking.payload.User;
import com.cg.bugtracking.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	

	@Transactional
	@Override
	public Employee createEmployee(Employee emp) {
		Employee employeeObj = null;
		try {
			employeeObj = employeeRepository.save(emp);

		} catch (Exception e) {
			throw new OperationFailedException(OPERATION_FAILED_CONST+ e.getMessage());
		}
		return employeeObj;
	}
	
	

	@Transactional
	@Override
	public Employee updateEmployee(long id, Employee employee) {
		Optional<Employee> employeeObj = null;
		Employee updatedEmployee = null;
		employeeObj = employeeRepository.findById(id);
		if (!employeeObj.isPresent()) {
			throw new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_CONST + id);
		} else {
			
			employeeObj.get().setEmpName(employee.getEmpName());
			employeeObj.get().setEmployeeEmail(employee.getEmployeeEmail());
			employeeObj.get().setBugList(employee.getBugList());
			employeeObj.get().setEmployeeContact(employee.getEmployeeContact());
			employeeObj.get().setEmpStatus(employee.getEmpStatus());

			try {
				updatedEmployee = employeeRepository.save(employeeObj.get());
			} catch (Exception e) {
				throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
			}

		}

		return updatedEmployee;
	}

	
	
	@Override
	public Employee deleteEmployee(long id) {
		Optional<Employee> employeeObj = employeeRepository.findById(id);
		if (!employeeObj.isPresent()) {
			
			throw new ResourceNotFoundException("Employee not found for this id: " + id);
			
		}
		
		else {
			try {
				employeeRepository.delete(employeeObj.get());
			}catch(Exception e) {
				throw new OperationFailedException("Delete operation failed"+e.getMessage());
			}			
		}
		
		return employeeObj.get();
	}

	
	
	@Override
	public Employee getEmployee(long id) {
		Optional<Employee> employee = employeeRepository.findById(id);

		if (!employee.isPresent())
			throw new ResourceNotFoundException("Employee not found for this id: " + id);

		return employee.get();
	}
	
	

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	



	@Override
	public String employeeLogin(User euser) {
		// TODO Auto-generated method stub
		String str = null;
		String employeeUid = euser.getUserId();

		Employee loginEmployee = employeeRepository.findByEmployee_userid(employeeUid);
		if (loginEmployee == null) {
			throw new LoginOperationException(USER_NOT_REGISTERED);
		} else {

			String loggedEmployeepswd = loginEmployee.getEmployeePassword();
			if (loggedEmployeepswd.equals(euser.getUserPassword())) {
				str = LOGIN_SUCCESS;
			} else {
				throw new LoginOperationException(LOGIN_FAIL);
			}
		}
		return str;
	}



	

}
