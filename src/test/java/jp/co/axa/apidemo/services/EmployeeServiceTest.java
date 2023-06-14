package jp.co.axa.apidemo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.RecordNotFoundException;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@SpringBootTest
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private MessageSource messageSource;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Mock
	private Logger log;

	private List<Employee> employeeList;
	private EmployeeDto employeeDto;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		employeeList = new ArrayList<>();
		employeeList.add(Employee.builder().id(1L).name("Trideb Chapagai").department("Non-Life Insurance")
				.salary(BigDecimal.valueOf(5000)).build());
		employeeList.add(Employee.builder().id(2L).name("Jane Smith").department("Finance")
				.salary(BigDecimal.valueOf(6000)).build());

		employeeDto = EmployeeDto.builder().id(1L).name("Trideb Chapagai").department("Non-Life Insurance")
				.salary(BigDecimal.TEN).build();
	}

	@Test
	void testRetrieveEmployees() {
		when(employeeRepository.findAll()).thenReturn(employeeList);

		List<EmployeeDto> employeeDtos = employeeService.retrieveEmployees();

		assertEquals(2, employeeDtos.size());
		assertEquals("Trideb Chapagai", employeeDtos.get(0).getName());
		assertEquals("Non-Life Insurance", employeeDtos.get(0).getDepartment());
		assertEquals(BigDecimal.valueOf(5000), employeeDtos.get(0).getSalary());
		assertEquals("Jane Smith", employeeDtos.get(1).getName());
		assertEquals("Finance", employeeDtos.get(1).getDepartment());
		assertEquals(BigDecimal.valueOf(6000), employeeDtos.get(1).getSalary());

		verify(employeeRepository, times(1)).findAll();
	}

	@Test
	void testRetrieveEmployees_EmptyList() {
		when(employeeRepository.findAll()).thenReturn(new ArrayList<>());
		List<EmployeeDto> employeeDtos = employeeService.retrieveEmployees();
		assertEquals(0, employeeDtos.size());
	}

	@Test
	void testGetEmployee() {
		Long employeeId = 1L;
		when(messageSource.getMessage(eq("error.employee.notfound"), isNull(), eq(Locale.ENGLISH)))
				.thenReturn("Employee not found");
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeList.get(0)));

		EmployeeDto employeeDto = employeeService.getEmployee(employeeId);

		assertEquals(employeeId, employeeDto.getId());
		assertEquals("Trideb Chapagai", employeeDto.getName());
		assertEquals("Non-Life Insurance", employeeDto.getDepartment());
		assertEquals(BigDecimal.valueOf(5000), employeeDto.getSalary());

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(messageSource, times(1)).getMessage(eq("error.employee.notfound"), isNull(), eq(Locale.ENGLISH));
	}

	@Test
	void testSaveEmployee() {
		Employee employee = Employee.builder().id(1L).name("Trideb Chapagai").department("Non-Life Insurance")
				.salary(BigDecimal.valueOf(5000)).build();
		when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

		employeeService.saveEmployee(employeeDto);

		verify(employeeRepository, times(1)).save(any(Employee.class));
	}

	@Test
	void testDeleteEmployee() {
		Long employeeId = 1L;
		doNothing().when(employeeRepository).deleteById(employeeId);

		employeeService.deleteEmployee(employeeId);

		verify(employeeRepository, times(1)).deleteById(employeeId);
	}

	@Test
	void testDeleteEmployee_RecordNotFoundException() {
		Long employeeId = 1L;
		doThrow(EmptyResultDataAccessException.class).when(employeeRepository).deleteById(employeeId);
		when(messageSource.getMessage(eq("error.employee.notfound.delete"), isNull(), eq(Locale.ENGLISH)))
				.thenReturn("Employee not found for deletion");

		assertThrows(RecordNotFoundException.class, () -> employeeService.deleteEmployee(employeeId));

		verify(employeeRepository, times(1)).deleteById(employeeId);
		verify(messageSource, times(1)).getMessage(eq("error.employee.notfound.delete"), isNull(), eq(Locale.ENGLISH));
	}

	@Test
	void testUpdateEmployee() {
		// Arrange
		Long employeeId = 1L;
		EmployeeDto employeeDto = EmployeeDto.builder().name("Trideb Chapagai").department("Non-Life Insurance")
				.salary(BigDecimal.valueOf(5000)).build();
		Employee employee = Employee.builder().id(1L).name("Existing Employee").department("Existing Department")
				.salary(BigDecimal.valueOf(1000)).build();

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

		// Act
		employeeService.updateEmployee(employeeDto, employeeId);

		// Assert
		verify(employeeRepository, times(1)).findById(employeeId);
		verify(employeeRepository, times(1)).save(employee);
		assertEquals("Trideb Chapagai", employee.getName());
		assertEquals("Non-Life Insurance", employee.getDepartment());
		assertEquals(BigDecimal.valueOf(5000), employee.getSalary());
	}

	@Test
	void testUpdateEmployee_RecordNotFoundException() {
		// Arrange
		Long employeeId = 1L;
		EmployeeDto employeeDto = EmployeeDto.builder().name("Trideb Chapagai").department("Non-Life Insurance")
				.salary(BigDecimal.valueOf(5000)).build();

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(RecordNotFoundException.class, () -> employeeService.updateEmployee(employeeDto, employeeId));

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(employeeRepository, never()).save(any());
	}

}
