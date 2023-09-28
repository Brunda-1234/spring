package com.te.springbatchcsvfileconverter.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListner implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution je) {

		
		System.out.println("Starting the batch :==" + je.getStatus());

	}

	@Override
	public void afterJob(JobExecution je) {
		// TODO Auto-generated method stub
		System.out.println("Starting the batch :==" + je.getStatus());

	}

}
