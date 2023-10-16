package com.saptak.scheduler;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saptak.model.JobRequest;
import com.saptak.service.JobService;

@Component
public class JobExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

	@Value("${cron.execution.switch}")
	private boolean runJobControlSwitch;

	@Autowired
	private JobService jobService;

	@Autowired
	private ObjectMapper objectMapper;

	@Scheduled(cron = "${cron.expression.interval}")
	void executePendingJobs() throws InterruptedException {

		LOGGER.info("Cronjob enabled: {}", runJobControlSwitch);
		if (!runJobControlSwitch)
			return;

		LOGGER.info("Calling DB -- Fetching oldest pending request");
		JobRequest jobRequest = jobService.fetchOldestPendingJobRequest();

		startJobProcessing(jobRequest);

	}

	@RabbitListener(queues = "JbrQueue")
	public void getPendingJobRequestFromQueue(JSONObject jsonRequest)
			throws JsonMappingException, JsonProcessingException, InterruptedException {

		JobRequest jobRequest = objectMapper.readValue(jsonRequest.toString(), JobRequest.class);

		startJobProcessing(jobRequest);
	}

	private void startJobProcessing(JobRequest jobRequest) throws InterruptedException {

		LOGGER.info("Locking Job ID: {}", jobRequest.getJobId());
		JobRequest lockedJobRequest = jobService.lockJobRequestStatus(jobRequest);

		LOGGER.info("Job {} execution starts===>", lockedJobRequest.getJobId());
		for (int i = 1; i <= 10; i++) {
			LOGGER.info("Job {} execution in progress..........", lockedJobRequest.getJobId());
			Thread.sleep(1000);
		}

		LOGGER.info("Job {} execution ends<===", lockedJobRequest.getJobId());

		LOGGER.info("Completing Job ID: {}", lockedJobRequest.getJobId());
		lockedJobRequest.setStatusCode("C");
		lockedJobRequest.setJbrUpdateTms(LocalDateTime.now());
		lockedJobRequest.setJbrUpdateOperId("SYSTEM");
		jobService.updateJobRequest(lockedJobRequest);

	}
}
