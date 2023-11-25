package com.example.imdbjpa;

import com.example.imdbjpa.entities.TitleBasics;
import com.example.imdbjpa.repo.TitleBasicsRepository;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.EnumSet;

@SpringBootApplication
public class ImdbJpaApplication implements CommandLineRunner {

@Autowired
private TitleBasicsRepository titleBasicsRepository;
//	private ServiceRegistry serviceRegistry;


	public static void main(String[] args) {
		SpringApplication.run(ImdbJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		titleBasicsRepository.findAll().forEach(System.out::println);

//		System.out.println("Running");
//
//		MetadataSources metadataSources = new MetadataSources(serviceRegistry);
//		metadataSources.addAnnotatedClass(TitleBasics.class);
//		Metadata metadata = metadataSources.buildMetadata();
//
//		SchemaExport schemaExport = new SchemaExport();
//		schemaExport.setFormat(true);
//		schemaExport.setOutputFile("create.sql");
//		schemaExport.createOnly(EnumSet.of(TargetType.SCRIPT), metadata);
	}
}
