package com.cg.bugtracking.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.cg.bugtracking.exceptions.CantAddEmployee;
import com.cg.bugtracking.exceptions.CantDeleteEmployee;
import com.cg.bugtracking.exceptions.OperationFailedException;
import com.cg.bugtracking.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.bugtracking.entity.Employee;
import com.cg.bugtracking.entity.Project;

import com.cg.bugtracking.repository.EmployeeRepository;
import com.cg.bugtracking.repository.ProjectRepository;
import static com.cg.bugtracking.util.AppConstant.PROJECT_NOT_FOUND_CONST;
import static com.cg.bugtracking.util.AppConstant.EMPLOYEE_NOT_FOUND_CONST;
import static com.cg.bugtracking.util.AppConstant.CANT_ADD_EMPLOYEE;
import static com.cg.bugtracking.util.AppConstant.OPERATION_FAILED_CONST;
import static com.cg.bugtracking.util.AppConstant.EMPLOYEE_NOT_PART;
import static com.cg.bugtracking.util.AppConstant.EMPLOYEE_ALERADY_PART;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Project createProject(Project project) {
		// TODO Auto-generated method stub
		Project projectObj = null;
		try {
			projectObj = projectRepository.save(project);

		} catch (Exception e) {
			throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
		}
		return projectObj;
	}

	@Override
	public List<Project> getAllProject() {

		List<Project> projectList = null;
		projectList = projectRepository.findAll();
		projectList.sort(Comparator.comparing(Project::getProjectId));
		return projectList;
	}

	@Override
	public Project findProject(long projectId) {

		Optional<Project> project = projectRepository.findById(projectId);

		if (!project.isPresent())
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND_CONST + projectId);

		return project.get();
	}

	@Transactional
	@Override
	public Project deleteProject(long projectId) {
		Optional<Project> productObj = projectRepository.findById(projectId);
		if (!productObj.isPresent()) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND_CONST + projectId);
		} else {
			try {
				projectRepository.delete(productObj.get());
			} catch (Exception e) {
				throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
			}
		}

		return productObj.get();
	}

	@Transactional
	@Override
	public Project addEmployeeProject(long projectId, long employeeId) {
		// TODO Auto-generated method stub
		Optional<Project> projectObj = projectRepository.findById(projectId);
		Optional<Employee> employeeObj = employeeRepository.findById(employeeId);
		if (!projectObj.isPresent()) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND_CONST + projectId);
		}
		if (!employeeObj.isPresent()) {
			throw new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_CONST + employeeId);
		}
		Employee emp = employeeObj.get();
		Project proj = projectObj.get();

		if (projectObj.get().getMembers().contains(emp)) {
			throw new CantAddEmployee(EMPLOYEE_ALERADY_PART + employeeId);
		}

		if (emp.getEmpStatus().equals("Assigned")) {
			throw new CantAddEmployee(CANT_ADD_EMPLOYEE + employeeId);
		}
		emp.setEmpStatus("Assigned");
		proj.getMembers().add(emp);
		
		return proj;
	}

	@Transactional
	@Override
	public Project deleteEmployeeProject(long projectId, long employeeId) {
		// TODO Auto-generated method stub
		Optional<Project> projectObj = projectRepository.findById(projectId);
		Optional<Employee> employeeObj = employeeRepository.findById(employeeId);

		if (!projectObj.isPresent()) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND_CONST + projectId);
		}
		if (!employeeObj.isPresent()) {
			throw new ResourceNotFoundException(EMPLOYEE_NOT_FOUND_CONST + employeeId);
		}
		Employee emp = employeeObj.get();
		Project proj = projectObj.get();
		if (!projectObj.get().getMembers().contains(emp)) {
			throw new CantDeleteEmployee(EMPLOYEE_NOT_PART + employeeId);
		}

		emp.getBugList().clear();
		emp.setEmpStatus("free");
		proj.getMembers().remove(emp);
		
		return proj;
	}

	@Transactional
	@Override
	public Project updateProject(long projectId, Project project) {
		Optional<Project> projectObj = null;
		Project updatedProject = null;
		projectObj = projectRepository.findById(projectId);
		if (!projectObj.isPresent()) {
			throw new ResourceNotFoundException(PROJECT_NOT_FOUND_CONST + projectId);
		} else {
			projectObj.get().setProjectName(project.getProjectName());
			projectObj.get().setProjectOwner(project.getProjectOwner());
			projectObj.get().setStatus(project.getStatus());
			projectObj.get().setBugList(project.getBugList());
			try {
				updatedProject = projectRepository.save(projectObj.get());
			} catch (Exception e) {
				throw new OperationFailedException(OPERATION_FAILED_CONST + e.getMessage());
			}
		}

		return updatedProject;
	}
}
