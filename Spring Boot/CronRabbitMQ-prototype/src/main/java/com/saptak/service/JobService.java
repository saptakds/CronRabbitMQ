package com.saptak.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saptak.model.JobRequest;
import com.saptak.repository.JobRequestRepository;

@Service
public class JobService {

	@Autowired
	private JobRequestRepository jobRequestRepository;

	public JobRequest createJobRequestService(JobRequest request) {
		request.setJbrUpdateOperId(request.getJbrInsertOperId());
		request.setJbrInsertTms(LocalDateTime.now());
		request.setJbrUpdateTms(LocalDateTime.now());
		return jobRequestRepository.save(request);
	}
	
	public List<JobRequest> fetchAllJobsForStatus(String statusCode){
		return jobRequestRepository.findAllByStatusCode(statusCode);
	}
}
