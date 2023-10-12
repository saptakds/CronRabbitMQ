package com.saptak.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saptak.model.JobRequest;
import com.saptak.service.JobService;

@Component
public class JobExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

	@Value("${cron.execution.switch}")
	private boolean runJobControlSwitch;

	@Autowired
	private JobService jobService;

	@Scheduled(cron = "${cron.expression.interval}")
	void executePendingJobs() throws InterruptedException {

		LOGGER.info("Cronjob enabled: {}", runJobControlSwitch);
		if (!runJobControlSwitch)
			return;

		LOGGER.info("Calling DB -- Fetching oldest pending request");
		JobRequest jobRequest = jobService.fetchOldestPendingJobRequest();

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
