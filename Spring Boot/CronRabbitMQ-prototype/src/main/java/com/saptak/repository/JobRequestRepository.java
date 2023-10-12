package com.saptak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saptak.model.JobRequest;

public interface JobRequestRepository extends JpaRepository<JobRequest, Integer> {

	List<JobRequest> findAllByStatusCode(String statusCode);

}
