package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The data access layer, employee repository responsible for database
 * operation.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
