package com.cg.bugtracking.controller;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.cg.bugtracking.entity.Project;

import com.cg.bugtracking.payload.BaseResponse;

import com.cg.bugtracking.service.ProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/project")
@Validated
@Api(value="Project Controller", description="Operations on Project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;	
	
	
	@PostMapping("/")
	@ApiOperation(value = "Add a Project")
	public ResponseEntity<?> createProject(@Valid @RequestBody Project project){
		
		Project projectObj = projectService.createProject(project);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(projectObj);		
		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "Show All Project")
	public ResponseEntity<?> fetchAllProjects() {
		
		List<Project> project = projectService.getAllProject();	

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(project);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	@GetMapping("/{id}")
	@ApiOperation(value = "Search a project with an ID",response = Project.class)
	public ResponseEntity<?> fetchProductDetails(@PathVariable("id") @Min(1)long id) {
	
		Project	project = projectService.findProject(id);	
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(project);	
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);		
		
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete a Project")
	public ResponseEntity<?> deleteProject(@PathVariable("id")@Min(1) long projectId) {
		
		Project projectObj = projectService.deleteProject(projectId);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(projectObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);			
	}
	
		
	@PutMapping("/Addemp/{pid}/{eid}")
	@ApiOperation(value = "Add employee project")
	public ResponseEntity<?> addEmployeeproject(@PathVariable("pid") @Min(1)long projectId,@PathVariable("eid") @Min(1)long employeeId) {
			
		Project updatedprojectObj = projectService.addEmployeeProject(projectId,employeeId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(updatedprojectObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
	@PutMapping("/DeleteEmp/{pid}/{eid}")
	@ApiOperation(value = "delete employee project")
	public ResponseEntity<?> deleteEmployeeproject(@PathVariable("pid") @Min(1)long projectId,@PathVariable("eid") @Min(1)long employeeId) {
		Project updatedprojectObj = projectService.deleteEmployeeProject(projectId,employeeId);
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(updatedprojectObj);		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
	@PutMapping("/{id}")
	@ApiOperation(value = "Update a project")
	public ResponseEntity<?> updateproject(@PathVariable("id") @Min(1)long projectId,@Valid @RequestBody Project project) {
			
        Project projectObj = projectService.updateProject(projectId,project);
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatusCode(1);
		baseResponse.setResponse(projectObj);	
		
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);				
	}
}