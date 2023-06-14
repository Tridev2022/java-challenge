package jp.co.axa.apidemo.services;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.RecordNotFoundException;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements operations of the employee details.
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Retrieve employees.
	 *
	 * @return the list
	 */
	public List<EmployeeDto> retrieveEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		if (employees.isEmpty()) {
			log.info("Retrieved employee size is empty");
		}
		// Convert to employee DTO object and return immediately.
		return employees.stream().map(this::convertToEmployeeDto).collect(Collectors.toList());
	}

	/**
	 * Gets the employee.
	 *
	 * @param employeeId the employee id
	 * @return the employee
	 */
	@Cacheable(cacheNames = "employee", key = "#employeeId")
	public EmployeeDto getEmployee(Long employeeId) {
		String errorMessage = messageSource.getMessage("error.employee.notfound", null, Locale.ENGLISH);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RecordNotFoundException(errorMessage));
		return convertToEmployeeDto(employee);
	}

	/**
	 * Save employee.
	 *
	 * @param employee the employee
	 */
	public void saveEmployee(EmployeeDto employeeDto) {
		employeeRepository.save(convertToEmployeeEntity(employeeDto));
	}

	@CacheEvict(cacheNames = "employee", key = "#employeeId")
	public void deleteEmployee(Long employeeId) {
		try {
			employeeRepository.deleteById(employeeId);
		} catch (EmptyResultDataAccessException exception) {
			log.error("Error on delete employee");
			String errorMessage = messageSource.getMessage("error.employee.notfound.delete", null, Locale.ENGLISH);
			throw new RecordNotFoundException(errorMessage);
		}
	}

	/**
	 * Update employee.
	 *
	 * @param employeeDto the employee dto
	 * @param employeeId  the employee id
	 */
	@CacheEvict(value = "employees", allEntries = true)
	public void updateEmployee(EmployeeDto employeeDto, Long employeeId) {
		String errorMessage = messageSource.getMessage("error.employee.notfound.update", null, Locale.ENGLISH);
		// Check whether the employee is present or not
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RecordNotFoundException(errorMessage));
		employee.setName(employeeDto.getName());
		employee.setSalary(employeeDto.getSalary());
		employee.setDepartment(employeeDto.getDepartment());
		employeeRepository.save(employee);
	}

	private EmployeeDto convertToEmployeeDto(Employee employee) {
		return EmployeeDto.builder().id(employee.getId()).name(employee.getName()).salary(employee.getSalary())
				.department(employee.getDepartment()).build();
	}

	private Employee convertToEmployeeEntity(EmployeeDto employeeDto) {
		return Employee.builder().id(employeeDto.getId()).name(employeeDto.getName()).salary(employeeDto.getSalary())
				.department(employeeDto.getDepartment()).build();
	}
}