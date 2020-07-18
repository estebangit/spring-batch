package com.esteban.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

//    @Bean
//    public FlatFileItemReader<PersonA> reader() {
//        return new FlatFileItemReaderBuilder<PersonA>()
//                .name("personItemReader")
//                .resource(new ClassPathResource("sample-data.csv"))
//                .delimited()
//                .names(new String[]{"firstName", "lastName"})
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<PersonA>() {{
//                    setTargetType(PersonA.class);
//                }})
//                .build();
//    }

    /**
     * Read data from source DB.
     *
     * @param dataSource The source data source.
     * @return The item reader.
     */
    @Bean
    public ItemReader<PersonA> itemReader(@Qualifier("dbSource") final DataSource dataSource) {
        JdbcCursorItemReader<PersonA> reader = new JdbcCursorItemReader<>();
        String FETCH_SQL_QUERY = " SELECT first_name, last_name from people_a";
        reader.setSql(FETCH_SQL_QUERY);
        reader.setDataSource(dataSource);
        reader.setRowMapper(new PersonARowMapper());
        return reader;
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /**
     * Persist into destination DB the data read previously.
     *
     * @param dataSource The destination data source.
     * @return The item writer.
     */
    @Bean
    public JdbcBatchItemWriter<PersonB> writer(@Qualifier(value = "dbDestination") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<PersonB>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people_b (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importDataJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importDataJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    //    @Bean
//    public Step step1(JdbcBatchItemWriter<PersonB> writer) {
//        return stepBuilderFactory.get("step1")
//                .<PersonA, PersonB>chunk(10)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer)
//                .build();
//    }
    @Bean
    public Step step1(ItemReader<PersonA> itemReader, JdbcBatchItemWriter<PersonB> writer) {
        return stepBuilderFactory.get("step1")
                .<PersonA, PersonB>chunk(10)
                .reader(itemReader)
                .processor(processor())
                .writer(writer)
                .build();
    }

}
