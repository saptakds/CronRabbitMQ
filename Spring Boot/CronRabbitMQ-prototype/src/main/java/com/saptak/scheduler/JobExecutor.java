package com.saptak.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saptak.model.JobRequest;
import com.saptak.service.JobService;

@Component
public class JobExecutor {

	@Value("${cron.execution.switch}")
	private boolean runJobControlSwitch;

	@Autowired
	private JobService jobService;

	@Scheduled(cron = "${cron.expression.interval}")
	void executePendingJobs() {
		List<JobRequest> allPendingJobRequests=jobService.fetchAllJobsForStatus("P");
	}
}
