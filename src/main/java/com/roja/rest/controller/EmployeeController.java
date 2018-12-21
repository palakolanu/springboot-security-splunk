package com.roja.rest.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.roja.rest.dao.EmployeeDAO;
import com.roja.rest.model.Employee;
import com.roja.rest.model.Employees;
import com.roja.splunk.SplunkUtil;
import com.splunk.Args;
import com.splunk.Receiver;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeDAO employeeDao;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@GetMapping(path = "/", produces = "application/json")
	public String getHello() {
		LOGGER.debug("Entering Into getHello method ");
		return "Welcome To Spring Security";
	}

	@GetMapping(path = "/employees", produces = "application/json")
	public Employees getEmployees() {
		LOGGER.debug("Entering Into getEmployees method ");

		return employeeDao.getAllEmployees();
	}

	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> addEmployee(
			@RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
			@RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation,
			@RequestBody Employee employee) throws Exception {
		LOGGER.debug("Entering Into addEmployee method ");
		// Generate resource id
		Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
		employee.setId(id);
		LOGGER.debug("Generated employee ID " + id);

		/* Pushing data to Splunk Server */
		
		// Set Source information
		Args logArgs = new Args();
		logArgs.put("sourcetype", "fromSpringboot");
		// Set Index
		Receiver receiver = SplunkUtil.connectToSplunk().getReceiver();
		receiver.log("main", logArgs, "Generated employee ID " + id);

		employeeDao.addEmployee(employee);

		LOGGER.debug("Added new employee : " + employee.getFirstName());
		receiver.log("main", logArgs, "Added new employee : " + employee.getFirstName());

		// Create resource location
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId())
				.toUri();

		// Send location in response
		return ResponseEntity.created(location).build();
	}

}
