package com.saptak.service;

import java.time.LocalDateTime;

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

	public JobRequest fetchOldestPendingJobRequest() {
		return jobRequestRepository.findFirstByStatusCodeOrderByJbrInsertTms("P");
	}

	public JobRequest lockJobRequestStatus(JobRequest request) {
		request.setJbrUpdateTms(LocalDateTime.now());
		request.setStatusCode("I");
		request.setJbrUpdateOperId("SYSTEM");
		return jobRequestRepository.save(request);
	}

	public JobRequest updateJobRequest(JobRequest request) {
		return jobRequestRepository.save(request);
	}
}
