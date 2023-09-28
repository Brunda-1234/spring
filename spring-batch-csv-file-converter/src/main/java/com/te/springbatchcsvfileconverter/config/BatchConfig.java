package com.te.springbatchcsvfileconverter.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;

import com.te.springbatchcsvfileconverter.entity.Product;
import com.te.springbatchcsvfileconverter.listener.MyJobListner;
import com.te.springbatchcsvfileconverter.processor.ProductProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	// Reader Class object
	@Bean /*
			 * this will create the object in the ioc container
			 */
	public FlatFileItemReader<Product> reader() {
		/*
		 * 1]
		 */

		FlatFileItemReader<Product> reader = new FlatFileItemReader<>();
		/*
		 * Resource is an interface
		 */
		reader.setResource(new ClassPathResource("products.csv")); // src/main/resources inside project
//		reader.setResource(new FileSystemResource("‪C:\\Users\\TYSS-1\\Downloads\\products.csv")); 
		// System driver --inside ur system
//		reader.setResource(new UrlResource("http:abcd.com//files//products.csv")); //url internet location

		reader.setLineMapper(new DefaultLineMapper<>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setDelimiter(DELIMITER_COMMA);// what is their in the file we should mention that
														// only(deliminator)
						setNames("prodId", "prodCode", "prodCost");
						// based on these things it will create the object
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
					{
						setTargetType(Product.class);
						// convert to this object
					}
				});
			}
		});
		return reader;
	}

	// process class object
	@Bean
	public ItemProcessor<Product, Product> processor() {
		return new ProductProcessor();
//		return item->{
//			double cost = item.getProdCost();
//			item.setProdDic(cost*12/100.0);
//			item.setProdGst(cost*22/100.0);
//			return item;
//			if we use this method no need to create a separate class
//		};
	}

	@Autowired
	private DataSource dataSource;

	/*
	 * DataSource is an Interface
	 */
	// writer class object
	@Bean
	public JdbcBatchItemWriter<Product> writer() {
		JdbcBatchItemWriter<Product> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		// query to set the data from the source
		writer.setSql(
				"INSERT INTO PRODUCTS(PID,PCODE,PCOST,PDISC,PGST) VALUES (:prodId,:prodCode,:prodCost,:prodDic,:prodGst)");
		// read the data to set the data to query
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		/*
		 * ItemSqlParameterSourceProvider [I] BeanPropertyItemSqlParameterSourceProvider
		 * : this is the implementation class of ItemSqlParameterSourceProvider this
		 * will match the source names to the sql parameters
		 * 
		 */
		return writer;
	}

	// listner class object
	@Bean
	public JobExecutionListener listner() {

		return  new MyJobListner();
//		return new JobExecutionListener() {
//
//			@Override
//			public void beforeJob(JobExecution je) {
//
//				System.out.println("Starting the batch :==" + je.getStatus());
//
//			}
//
//			@Override
//			public void afterJob(JobExecution je) {
//				// TODO Auto-generated method stub
//				System.out.println("Starting the batch :==" + je.getStatus());
//
//			}
//		}; if we use this anonymous class no need to create a separate class
	}

	// autowire step builder factory
	@Autowired
	private StepBuilderFactory sf;

	// step object
	@Bean
	public Step stepA() {
		return sf.get("stepA") //step name given step name and method name should be same
				.<Product,Product>chunk(3)//it will take only 3 records at a time
				.reader(reader())//read obj
				.processor(processor())//processor obj
				.writer(writer())//write obj
				.build();
	}

	// autowire job builderFactory
	@Autowired
	private JobBuilderFactory jf;

	// job object
	@Bean
	public Job jobs() {
		return jf.get("jobs")
				.incrementer( new RunIdIncrementer())
				.listener(listner())
				.start(stepA())
//				.next(stepB()) these are next steps we need to call one by one in future 
//				.next(stepC())
				.build();
	}

}
