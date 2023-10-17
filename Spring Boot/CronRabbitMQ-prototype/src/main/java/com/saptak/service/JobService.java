package com.saptak.service;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saptak.constant.ConstantValues;
import com.saptak.model.JobRequest;
import com.saptak.repository.JobRequestRepository;

@Service
public class JobService {

	@Value("${rabbitproducer.enabled}")
	private boolean rabbitMqSwitch;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public JobRequest createJobRequestService(JobRequest request) {
		request.setJbrUpdateOperId(request.getJbrInsertOperId());
		request.setJbrInsertTms(LocalDateTime.now());
		request.setJbrUpdateTms(LocalDateTime.now());

		JobRequest createdJobRequest = jobRequestRepository.save(request);

		if (rabbitMqSwitch) {
			JSONObject requestJson = new JSONObject(createdJobRequest);
			rabbitTemplate.convertAndSend(ConstantValues.JOB_QUEUE_NAME, requestJson.toString());
		}

		return createdJobRequest;
	}

	public JobRequest fetchOldestPendingJobRequest() {
		return jobRequestRepository.findFirstByStatusCodeOrderByJbrInsertTms("P");
	}

	public JobRequest lockJobRequestStatus(JobRequest request) {
		request.setJbrUpdateTms(LocalDateTime.now());
		request.setStatusCode(ConstantValues.JOB_STATUS_CODE_INPROGRESS);
		request.setJbrUpdateOperId(ConstantValues.SYSTEM_OPERATOR_ID);
		return jobRequestRepository.save(request);
	}

	public JobRequest updateJobRequest(JobRequest request) {
		return jobRequestRepository.save(request);
	}
}
