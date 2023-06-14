package jp.co.axa.apidemo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDto {

	private Long id;
	
	@NotEmpty
	private String name;
	
	@DecimalMin(value = "1.0")
    @Digits(integer=3, fraction=2)
	private BigDecimal salary;
	
	@NotEmpty
	private String department;

}
