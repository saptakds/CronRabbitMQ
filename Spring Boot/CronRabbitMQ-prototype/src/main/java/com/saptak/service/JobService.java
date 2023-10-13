package com.saptak.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saptak.model.JobRequest;
import com.saptak.repository.JobRequestRepository;

@Service
public class JobService {

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MessageService messageService;

	public JobRequest createJobRequestService(JobRequest request) throws IOException {
		request.setJbrUpdateOperId(request.getJbrInsertOperId());
		request.setJbrInsertTms(LocalDateTime.now());
		request.setJbrUpdateTms(LocalDateTime.now());
		JobRequest createdJobRequest = jobRequestRepository.save(request);
//		Message message = MessageBuilder.withBody(messageService.messageInByteArray(createdJobRequest)).build();
//		rabbitTemplate.send("JbrQueue", message);
		rabbitTemplate.convertAndSend("JbrQueue", request);
		return createdJobRequest;
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
