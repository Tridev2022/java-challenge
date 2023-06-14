package jp.co.axa.apidemo.services;

import java.util.List;

import jp.co.axa.apidemo.model.EmployeeDto;

/**
 * Employee service layer class use for the Employee operations.
 */
public interface EmployeeService {

	/**
	 * This method returns all employee records from database.
	 *
	 * @return the list
	 */
	public List<EmployeeDto> retrieveEmployees();

	/**
	 * This method fetch the employee records from database by given employee id.
	 *
	 * @param employeeId the employee id
	 * @return the employee
	 */
	public EmployeeDto getEmployee(Long employeeId);

	/**
	 * This method inserting the employee records to the database.
	 *
	 * @param employeeDto the employee dto
	 */
	public void saveEmployee(EmployeeDto employeeDto);

	/**
	 * This method removing the employee record from database.
	 *
	 * @param employeeId the employee id
	 */
	public void deleteEmployee(Long employeeId);

	/**
	 * This method updating the existing employee details in the database.
	 *
	 * @param employeeDto the employee dto
	 * @param employeeId  the employee id
	 */
	public void updateEmployee(EmployeeDto employeeDto, Long employeeId);
}