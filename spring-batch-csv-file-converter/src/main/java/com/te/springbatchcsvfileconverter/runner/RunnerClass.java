package com.te.springbatchcsvfileconverter.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RunnerClass implements CommandLineRunner {

	@Autowired
	private JobLauncher launcher;
	@Autowired
	private Job job;
	@Override
	public void run(String... args) throws Exception {
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time",System.currentTimeMillis())
				.toJobParameters();
		launcher.run(job, jobParameters);
		System.err.println("Job Execution Done");
	}

}
