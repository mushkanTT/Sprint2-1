package com.cg.bugtracking.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.cg.bugtracking.entity.Project;
import com.cg.bugtracking.payload.BaseResponse;
import com.cg.bugtracking.service.BugService;
import com.cg.bugtracking.service.EmployeeService;
import com.cg.bugtracking.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/bug")
@Api(value="Bug Controller", description="Operations on Bug")
public class BugController {
	
	@Autowired
	private BugService bugService;	
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/")
	@ApiOperation(value = "Add a bug")
	public ResponseEntity<?> createBug(@Valid @RequestBody Bug bug){
		
		Bug bugObj = bugService.createBug(bug);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugObj);		
		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
	}
	
	@PutMapping("/assign-project/{pid}/{bid}")
	@ApiOperation(value = "Assign a bug to project")
	public ResponseEntity<?> assignBugToProject(@PathVariable("pid") long projectId,@PathVariable("bid") long bugId) {
		Bug bug=bugService.	getBug(bugId);
		Project project=projectService.findProject(projectId);
		project.getBugList().add(bug);
		Project projectObj=projectService.updateProject(projectId, project);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(projectObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
	
	@PutMapping("/assign-employee/{pid}/{eid}/{bid}")
	@ApiOperation(value = "Assign a bug to employee")
	public ResponseEntity<?> assignBugToEmployee(@PathVariable("pid") long projectId,@PathVariable("eid") long employeeId,@PathVariable("bid") long bugId) {
		
		Project project=projectService.findProject(projectId);
		Employee employee=employeeService.getEmployee(employeeId);
		Bug bug=bugService.getBug(bugId);
		BaseResponse baseResponse = new BaseResponse();
		if(project.getMembers().contains(employee)&&project.getBugList().contains(bug)) {
			employee.getBugList().add(bug);
			Employee employeeObj=employeeService.updateEmployee(employeeId, employee);
			baseResponse.setStatusCode(1);
			baseResponse.setResponse(employeeObj);	
			return new ResponseEntity<>(baseResponse, HttpStatus.OK);	
		}
		else {
			baseResponse.setStatusCode(-1);
			baseResponse.setResponse("Bug or employee is not associated with project");
			return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);	
		}
					
	}

	
	@GetMapping("/{id}")
	@ApiOperation(value = "Search a bug with an ID",response = Bug.class)
	public ResponseEntity<?> getBug(@PathVariable("id") long bugId) {
	
		Bug	bugObj = bugService.getBug(bugId);	
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugObj);	
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Update a bug")
	public ResponseEntity<?> updateBug(@PathVariable("id") long bugId,@Valid @RequestBody Bug bug) {
			
		Bug bugObj = bugService.updateBug(bugId,bug);	
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete a bug")
	public ResponseEntity<?> deleteBug(@PathVariable("id") long bugId) {
		
		Bug bugObj = bugService.deleteBug(bugId);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);			
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "Show all bugs")
	public ResponseEntity<?> getAllBugs() {
		
		List<Bug> bugs = bugService.getAllBugs();	
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugs);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	
	@GetMapping("/getby/{status}")
	@ApiOperation(value = "Search a bug with status",response = Bug.class)
	public ResponseEntity<?> getAllBugsByStatus(@PathVariable("status") String status) {
		
		List<Bug> bugs = bugService.getAllBugsByStatus(status);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(bugs);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	
}
