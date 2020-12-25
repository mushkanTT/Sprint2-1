package com.cg.bugtracking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.boot.test.context.SpringBootTest;
import static com.cg.bugtracking.util.AppConstant.*;

import static org.mockito.Mockito.times;

import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import com.cg.bugtracking.entity.Employee;
import com.cg.bugtracking.entity.Project;
import com.cg.bugtracking.exceptions.ResourceNotFoundException;
import com.cg.bugtracking.repository.EmployeeRepository;
import com.cg.bugtracking.repository.ProjectRepository;
import com.cg.bugtracking.service.ProjectServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProjectTest {
	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private EmployeeRepository employeeRepository;
    @InjectMocks
	private ProjectServiceImpl projectService;
	

	@DisplayName(" Test Case for Creating a Project")
	@Test
	void testCreateProduct() {
		Project projectOne = new Project();
		projectOne.setProjectId(Long.valueOf(901));
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));

		given(projectRepository.save(projectOne)).willReturn(projectOne);
		Project savedProject = projectService.createProject(projectOne);
		Assertions.assertThat(savedProject).isNotNull();
		verify(projectRepository).save(any(Project.class));

	}

	@DisplayName("Test case for  finding a Project")
	@Test
	void testFindProjectById() {
		long projectId = 901;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));

		given(projectRepository.findById(projectId)).willReturn(Optional.of(projectOne));
		Project expected = projectService.findProject(projectId);
		Assertions.assertThat(expected).isNotNull();

	}
	@DisplayName(" Test Case for find all Project")
	@Test
	public void testFindAllProjects() {
		// given
		long projectId = 901;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));
		List<Project> expectedProducts = Arrays.asList(projectOne);

		// Mockito.doReturn(expectedProducts).when(productRepository).findAll();
		given(projectRepository.findAll()).willReturn(expectedProducts);

		// when
		List<Project> actualProducts = projectService.getAllProject();
		// then
		Assertions.assertThat(actualProducts).isEqualTo(expectedProducts);
		// assertEquals(actualProducts,expectedProducts);
	}
	@DisplayName(" Test Case for updating a Project")
	@Test
	void shouldUpdateProject() {
		long projectId = 901;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));

		given(projectRepository.save(projectOne)).willReturn(projectOne);
		given(projectRepository.findById(projectId)).willReturn(Optional.of(projectOne));

		Project expectedProject = projectService.updateProject(projectOne.getProjectId(), projectOne);

		Assertions.assertThat(expectedProject).isNotNull();

	}
	@DisplayName(" Test Case for deleting a Project")
	@Test
	public void shouldBeDeleted() {
		long projectId = 100;

		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));
		given(projectRepository.findById(projectId)).willReturn(Optional.of(projectOne));
		projectService.deleteProject(projectId);
		projectService.deleteProject(projectId);
		verify(projectRepository, times(2)).delete(projectOne);

	}
	
	@DisplayName(" Project Not Found Test case for find A project")
	
	@Test
	public void testFindProjectByIdWhenExceptionThrown() {

		int paramProjectId = 1000;
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			projectService.findProject(paramProjectId);
		});
		String expectedMessage = PROJECT_NOT_FOUND_CONST + paramProjectId;
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	

	
	@DisplayName(" Project Not Found Test case for update a project")
	@Test
	void NotFoundExceptionUpdateProject() {
		long projectId = 901;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));

		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			projectService.updateProject(projectOne.getProjectId(), projectOne);
		});
		String expectedMessage = PROJECT_NOT_FOUND_CONST + projectId;
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);

	}

	
	@DisplayName(" Project Not Found Test case for Deleting a project")
	@Test
	public void NotFoundDeleted() {

		int paramProjectId = 1000;
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			projectService.deleteProject(paramProjectId);
		});
		String expectedMessage = PROJECT_NOT_FOUND_CONST + paramProjectId;
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}
	@DisplayName(" add employee to a  project")
	@Test
	void addEmployeeProjectTest() {
		long projectId = 901;
		long employeeId = 101;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner(new String("capg"));
		projectOne.setStatus(new String("ongoing"));

		Employee employeeOne = new Employee();
		employeeOne.setEmployeeEmail("gmail");
		employeeOne.setEmployeeContact("09888");
		employeeOne.setEmpId(employeeId);
		employeeOne.setEmpName(new String("raj"));
		employeeOne.setEmpStatus(new String("free"));

		given(projectRepository.findById(projectId)).willReturn(Optional.of(projectOne));
		given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employeeOne));
		Project expected = projectService.addEmployeeProject(projectId, employeeId);
		//Assertions.assertThat(expected).isNotNull();

        assertEquals(1,expected.getMembers().size());
	}
	@DisplayName(" delete employee from a  project")
	@Test
	void deleteEmployeeProjectTest() {
		long projectId = 901;
		long employeeId = 101;
		Project projectOne = new Project();
		projectOne.setProjectId(projectId);
		projectOne.setProjectName("first");
		projectOne.setProjectOwner("capg");
		projectOne.setStatus("ongoing");

		Employee employeeOne = new Employee();
		employeeOne.setEmployeeEmail("gmail");
		employeeOne.setEmployeeContact("09888");
		employeeOne.setEmpId(employeeId);
		employeeOne.setEmpName("raj");
		employeeOne.setEmpStatus("Assigned");
		List<Employee> members =new  ArrayList<>();
		members.add(employeeOne);
		projectOne.setMembers(members);
		given(projectRepository.findById(projectId)).willReturn(Optional.of(projectOne));
		given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employeeOne));
		System.out.print(projectOne.getMembers().size());
		Project expected = projectService.deleteEmployeeProject(projectOne.getProjectId(), employeeOne.getEmpId());
		//Assertions.assertThat(expected).isNotNull();
		
        assertEquals(0,expected.getMembers().size());
	}
	

	

}
