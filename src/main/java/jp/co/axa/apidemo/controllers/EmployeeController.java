package jp.co.axa.apidemo.controllers;

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

import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller is entry point for employee related APIs operations.
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Returns the list of employees.
	 *
	 * @return the employees
	 */
	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeDto>> getEmployees() {
		List<EmployeeDto> employeeDtos = employeeService.retrieveEmployees();
		return new ResponseEntity<>(employeeDtos, HttpStatus.OK);
	}

	/**
	 * Gets the employee by employeeId.
	 *
	 * @param employeeId the employee id
	 * @return the employee
	 */
	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<EmployeeDto> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		EmployeeDto employeeDto = employeeService.getEmployee(employeeId);
		return new ResponseEntity<>(employeeDto, HttpStatus.OK);
	}

	/**
	 * Api is use for inserting the employee records.
	 *
	 * @param employeeDto the employee dto
	 * @return the response entity
	 */
	@PostMapping("/employees")
	public ResponseEntity<Object> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
		employeeService.saveEmployee(employeeDto);
		log.info("Employee Saved Successfully");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Api is use for removing the employee records.
	 *
	 * @param employeeId the employee id
	 * @return the response entity
	 */
	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		log.info("Employee Deleted Successfully");
		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Api use for Update employee records.
	 *
	 * @param employeeDto the employee dto
	 * @param employeeId the employee id
	 * @return the response entity
	 */
	@PutMapping("/employees/{employeeId}")
	public ResponseEntity<Object> updateEmployee(@RequestBody EmployeeDto employeeDto,
			@PathVariable(name = "employeeId") Long employeeId) {
		employeeService.updateEmployee(employeeDto, employeeId);
		log.info("Employee updated Successfully");
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
