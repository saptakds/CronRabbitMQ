package com.saptak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saptak.model.JobRequest;
import com.saptak.service.JobService;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;
	
	@PostMapping("/create")
	public JobRequest createJobRequest(@RequestBody JobRequest request) {
		return jobService.createJobRequestService(request);
	}
	
	
}
