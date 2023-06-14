package jp.co.axa.apidemo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.axa.apidemo.exception.RecordNotFoundException;
import jp.co.axa.apidemo.model.EmployeeDto;
import jp.co.axa.apidemo.services.EmployeeService;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	private static final String BASE_URL = "/api/v1/employees";

	private EmployeeDto firstEmployeeDto;
	private EmployeeDto secondEmployeeDto;

	@BeforeEach
	void setup() {
		firstEmployeeDto = EmployeeDto.builder().id(1L).name("Trideb chapagai").department("Non-Life Insurance Dept")
				.salary(BigDecimal.TEN).build();
		secondEmployeeDto = EmployeeDto.builder().id(2L).name("John Prange").department("Life Insurance Dept")
				.salary(BigDecimal.ONE).build();
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testGetEmployees() throws Exception {
		List<EmployeeDto> employees = Arrays.asList(firstEmployeeDto, secondEmployeeDto);

		when(employeeService.retrieveEmployees()).thenReturn(employees);

		mockMvc.perform(get(BASE_URL)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Trideb chapagai"))
				.andExpect(jsonPath("$[0].salary").value(BigDecimal.TEN))
				.andExpect(jsonPath("$[0].department").value("Non-Life Insurance Dept"))
				.andExpect(jsonPath("$[1].id").value(2)).andExpect(jsonPath("$[1].name").value("John Prange"))
				.andExpect(jsonPath("$[1].salary").value(BigDecimal.ONE))
				.andExpect(jsonPath("$[1].department").value("Life Insurance Dept"));
	}

	@Test
	void testGetEmployees_UnauthorizedAccess() throws Exception {
		List<EmployeeDto> employees = Arrays.asList(firstEmployeeDto, secondEmployeeDto);

		when(employeeService.retrieveEmployees()).thenReturn(employees);

		mockMvc.perform(get(BASE_URL)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testGetEmployees_GlobalException() throws Exception {
		when(employeeService.retrieveEmployees())
				.thenThrow(new RuntimeException("Oops! something went wrong, please contact your service provider."));

		mockMvc.perform(get(BASE_URL)).andExpect(status().isInternalServerError());

		verify(employeeService, times(1)).retrieveEmployees();
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testGetEmployee() throws Exception {
		when(employeeService.getEmployee(1L)).thenReturn(firstEmployeeDto);

		mockMvc.perform(get(BASE_URL + "/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Trideb chapagai"))
				.andExpect(jsonPath("$.salary").value(BigDecimal.TEN))
				.andExpect(jsonPath("$.department").value("Non-Life Insurance Dept"));
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testGetEmployee_RecordNotFoundException() throws Exception {
		when(employeeService.getEmployee(1L))
				.thenThrow(new RecordNotFoundException("Employee not found for your query, please check the input."));

		mockMvc.perform(get(BASE_URL + "/1")).andExpect(status().isNotFound());

		verify(employeeService, times(1)).getEmployee(1L);
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testSaveEmployee() throws Exception {
		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\":1,\"name\":\"Trideb chapagai\",\"salary\":10,\"department\":\"Non-Life Insurance Dept\"}"))
				.andExpect(status().isCreated());

		verify(employeeService, times(1)).saveEmployee(eq(firstEmployeeDto));
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testDeleteEmployee() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/1")).andExpect(status().isOk());

		verify(employeeService, times(1)).deleteEmployee(1L);
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testDeleteEmployee_RecordNotFoundException() throws Exception {

		doThrow(new RecordNotFoundException("Employee is not available for delete, please check the input."))
				.when(employeeService).deleteEmployee(1L);

		mockMvc.perform(delete(BASE_URL + "/1")).andExpect(status().isNotFound());

		verify(employeeService, times(1)).deleteEmployee(1L);
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testUpdateEmployee() throws Exception {

		mockMvc.perform(put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"Updated Name\", \"salary\": 100, \"department\": \"Updated Department\"}"))
				.andExpect(status().isOk());

		verify(employeeService, times(1)).updateEmployee(any(EmployeeDto.class), eq(1L));
	}

	@Test
	@WithMockUser(username = "ajx", password = "ajx", roles = "USER")
	void testUpdateEmployee_RecordNotFoundException() throws Exception {
		doThrow(new RecordNotFoundException("Employee is not available for update, please check the input."))
				.when(employeeService).updateEmployee(any(EmployeeDto.class), eq(1L));

		mockMvc.perform(put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"Updated Name\", \"salary\": 100, \"department\": \"Updated Department\"}"))
				.andExpect(status().isNotFound());

		verify(employeeService, times(1)).updateEmployee(any(EmployeeDto.class), eq(1L));
	}
}
