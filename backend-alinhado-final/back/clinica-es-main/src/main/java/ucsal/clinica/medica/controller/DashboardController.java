package ucsal.clinica.medica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.global.DashboardService;
import ucsal.clinica.medica.global.StatusDashboardResponse;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/status")
public class DashboardController {

	private final DashboardService service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public StatusDashboardResponse statusDashboard() {
		return service.statusDashboard();
	}

	
}
