package com.cg.bugtracking.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bugtracking.entity.Bug;
import com.cg.bugtracking.entity.Employee;
import com.cg.bugtracking.payload.BaseResponse;
import com.cg.bugtracking.service.BugService;
import com.cg.bugtracking.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/employee")
@Api(value="Employee Controller", description="Operations on employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;	
	
	@Autowired
	private BugService bugService;
	
	@PostMapping("/")
	@ApiOperation(value = "Add an employee")
	public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee){
		
		Employee employeeObj = employeeService.createEmployee(employee);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(employeeObj);		
		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Search an employee with an ID",response = Employee.class)
	public ResponseEntity<?> getEmployee(@PathVariable("id") @Min(1) long empId) {
	
		Employee employeeObj = employeeService.getEmployee(empId);	
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(employeeObj);	
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Update an employee")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") @Min(1) long empId,@Valid @RequestBody Employee employee) {
			
		Employee employeeObj = employeeService.updateEmployee(empId,employee);	
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(employeeObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete an employee")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") @Min(1) long empId) {
		
		Employee employeeObj = employeeService.deleteEmployee(empId);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(employeeObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);			
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "Show all employee")
	public ResponseEntity<?> getAllEmployees() {
		
		List<Employee> Employees = employeeService.getAllEmployees();	
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(Employees);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	

	
	
	@PutMapping("/remove-bug/{eid}/{bid}")
	@ApiOperation(value = "remove a bug from employee")
	public ResponseEntity<?> assignBugFromEmployee(@PathVariable("eid") long employeeId,@PathVariable("bid") long bugId) {
		Bug bug=bugService.getBug(bugId);
		Employee employee=employeeService.getEmployee(employeeId);
		BaseResponse baseResponse = new BaseResponse();
		if(employee.getBugList().contains(bug)) {
			employee.getBugList().remove(bug);
			Employee employeeObj=employeeService.updateEmployee(employeeId, employee);
			baseResponse.setStatusCode(1);
			baseResponse.setResponse(employeeObj);	
			return new ResponseEntity<>(baseResponse, HttpStatus.OK);
		}
		else {
			baseResponse.setStatusCode(1);
			baseResponse.setResponse("Employee is not assigned with bugId:"+bugId);
			return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
			
		}
	}
				
}
	
	
	
