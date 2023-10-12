package com.saptak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saptak.model.JobRequest;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, Integer> {

	JobRequest findFirstByStatusCodeOrderByJbrInsertTms(String statusCode);

}
