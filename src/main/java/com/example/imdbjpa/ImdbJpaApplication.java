package com.example.imdbjpa;

import com.example.imdbjpa.entities.AbstractEntity;
import com.example.imdbjpa.entities.TitleBasics;
import com.example.imdbjpa.repo.TitleBasicsRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.imdbjpa.DataReader.*;


//2023-11-25T17:57:24.810Z  INFO 40516 --- [           main] com.example.imdbjpa.ImdbJpaApplication   : Started ImdbJpaApplication in 2.455 seconds (process running for 2.724)


@SpringBootApplication
public class ImdbJpaApplication implements CommandLineRunner {

	@Autowired
	private TitleBasicsRepository titleBasicsRepository;
	// private ServiceRegistry serviceRegistry;

	public static void main(String[] args) {
		SpringApplication.run(ImdbJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		loadTitleBasics();

		titleBasicsRepository.findAll().forEach(System.out::println);
		TitleBasics t = null;

	}

	private <T  extends AbstractEntity<I>,I> void loadData(String fileName, Function<String[],T> fn, JpaRepository<T,I> repo)
	{
		
	}

	private void loadTitleBasics() throws FileNotFoundException, IOException {
		try (BufferedReader reader = DataReader.getReader("title.basics.tsv.gz")) {
			AtomicLong count = new AtomicLong();
			long startTime =System.currentTimeMillis();
			List<TitleBasics> buffer = new ArrayList<>();
			reader.lines().forEach(line -> {
				if (count.get() > 0) {

					String[] split = line.split("\t");

					TitleBasics titleBasics = TitleBasics.builder()
							.tconst(split[0])
							.titleType(split[1])
							.primaryTitle(split[2])
							.originalTitle(split[3])
							.isAdult(!split[4].trim().equals("0"))
							.startYear(getInt(split[5]))
							.endYear(getInt(split[6]))
							.runtimeMinutes(getInt(split[7]))
							.genres(split[8])
							.build();
					buffer.add(titleBasics);
					
					if(count.get() % 1000==1)
					{
						titleBasicsRepository.saveAll(buffer);
						buffer.clear();

						System.out.println("Saved + "+count.get() +" Time : " + (System.currentTimeMillis()-startTime));

					}
				}
				count.incrementAndGet();

			});
			titleBasicsRepository.saveAll(buffer);
		}
	}
}

//Saved + 10234001 Time : 1174216						Fd
