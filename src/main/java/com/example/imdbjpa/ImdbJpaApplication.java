package com.example.imdbjpa;

import com.example.imdbjpa.entities.TitleAka;
import com.example.imdbjpa.entities.TitleBasics;
import com.example.imdbjpa.repo.TitleAkaRepository;
import com.example.imdbjpa.repo.TitleBasicsRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.example.imdbjpa.DataReader.*;

//2023-11-25T17:57:24.810Z  INFO 40516 --- [           main] com.example.imdbjpa.ImdbJpaApplication   : Started ImdbJpaApplication in 2.455 seconds (process running for 2.724)

@SpringBootApplication
@EnableAsync
public class ImdbJpaApplication implements CommandLineRunner {

    static final int SIZE = 1000;

    @Autowired
    private TitleBasicsRepository titleBasicsRepository;
//Thread[#39,pool-2-thread-3,5,main]: Saved + 37892855 Time : 1078652

    // private ServiceRegistry serviceRegistry;

    //  @Autowired
    //  private TitleAkaCrudRepository titleAkaCrudRepository;

    @Autowired
    private TitleAkaRepository titleAkaRepository;


    public static void main(String[] args) {
        SpringApplication.run(ImdbJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // loadTitleBasics();

    //    loadDataAsync1("title.akas.tsv.gz", TitleAka::map);
        loadDataFast1("title.akas.tsv.gz", TitleAka::map, titleAkaRepository);

        //titleBasicsRepository.findAll().forEach(System.out::println);
        // TitleBasics t = null;

    }




    public <T, I> void loadDataFast1(String fileName, Function<String[], T> fn, JpaRepository<T, I> repo)
            throws FileNotFoundException, IOException, InterruptedException, ExecutionException {

        loadDataFast(fileName, fn.andThen(List::of), repo);
    }

    public <T, I> void loadData(String fileName, Function<String[], T> fn, JpaRepository<T, I> repo)
            throws FileNotFoundException, IOException {

        try (BufferedReader reader = DataReader.getReader(fileName)) {
            AtomicLong count = new AtomicLong(1);
            long startTime = System.currentTimeMillis();
            List<T> buffer = new ArrayList<>();
            reader.lines().skip(1).forEach(line -> {

                String[] split = line.split("\t");

                T t = fn.apply(split);

                buffer.add(t);

                if (count.get() % 10000 == 0) {
                    repo.saveAll(buffer);
                    buffer.clear();

                    System.out
                            .println("Saved + " + count.get() + " Time : " + (System.currentTimeMillis() - startTime));

                }

                count.incrementAndGet();

            });
            repo.saveAll(buffer);
        }

    }

    public <T, I> void loadDataFast(String fileName, Function<String[], List<T>> fn, JpaRepository<T, I> repo)
            throws FileNotFoundException, IOException, InterruptedException, ExecutionException {

        try (BufferedReader reader = DataReader.getReader(fileName) ) {

            ExecutorService service = Executors.newFixedThreadPool(8);

            final ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(20000);
            long startTime = System.currentTimeMillis();
          //  long count = 0;
            final AtomicLong saved = new AtomicLong(0);
            String line;
            //List<Future<?>> futures = new ArrayList<>();
            reader.readLine();

            AtomicBoolean pending = new AtomicBoolean(true);

            Runnable runnable = () -> {
                System.out.println("Started on Thread " + Thread.currentThread());
                List<T> buffer = new ArrayList<>();
                T t;
                while ((t = queue.poll()) != null || pending.get()) {
                    if (t != null) {
                        buffer.add(t);
                        if (buffer.size() > SIZE) {
                            repo.saveAll(buffer);

                            long thisCycle = saved.addAndGet(buffer.size());

                            buffer.clear();
                            System.out
                                    .println(Thread.currentThread() + ": Saved + " + thisCycle + " Time : "
                                            + (System.currentTimeMillis() - startTime));

                        }
                    }

                }

            };
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);
            service.submit(runnable);

            while ((line = reader.readLine()) != null) {
              //  count++;
                for (T t : fn.apply(line.split("\t"))) {
                    queue.put(t);
                }
            }
            pending.set(false);
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);

        }

    }


    private void loadTitleBasics() throws FileNotFoundException, IOException {

        try (BufferedReader reader = DataReader.getReader("title.basics.tsv.gz")) {
            AtomicLong count = new AtomicLong();
            long startTime = System.currentTimeMillis();
            List<TitleBasics> buffer = new ArrayList<>();
            reader.lines().forEach(line -> {
                if (count.get() > 0) {

                    String[] split = line.split("\t");

                    TitleBasics titleBasics = TitleBasics.builder().tconst(split[0]).titleType(split[1])
                            .primaryTitle(split[2]).originalTitle(split[3]).isAdult(!split[4].trim().equals("0"))
                            .startYear(getInt(split[5])).endYear(getInt(split[6])).runtimeMinutes(getInt(split[7]))
                            .genres(split[8]).build();
                    buffer.add(titleBasics);

                    if (count.get() % 1000 == 1) {
                        titleBasicsRepository.saveAll(buffer);
                        buffer.clear();

                        System.out.println(
                                "Saved + " + count.get() + " Time : " + (System.currentTimeMillis() - startTime));

                    }
                }
                count.incrementAndGet();

            });
            titleBasicsRepository.saveAll(buffer);
        }
    }

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("pg-");
		executor.initialize();
		return executor;
	}
}

// Saved + 10234001 Time : 1174216 Fd
