package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@SpringBootApplication
@EnableScheduling
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
    }

    @Bean
    ThreadPoolTaskSchedulerCustomizer threadPoolTaskSchedulerPhaseCustomizer() {
        return p -> {
            p.setPhase(0);
            p.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
            p.setWaitForTasksToCompleteOnShutdown(true);
            p.setAcceptTasksAfterContextClose(true);
            p.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
            p.setAwaitTerminationSeconds(180);
            p.setPoolSize(4);
            p.setThreadNamePrefix("custom-scheduler-");
        };
    }

    @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
    TaskScheduler taskScheduler(ThreadPoolTaskSchedulerBuilder builder) {
        //This is the default task scheduler used by ScheduledAnnotationBeanPostProcessor, we use this to prevent "No TaskScheduler/ScheduledExecutorService bean found for scheduled processing"
        return builder

                .build();
    }

    @Scheduled(fixedRate = 1000, scheduler = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
    public void test() {

        var pid = System.getProperty("PID");
        System.out.println("Kill me with 'kill -int " + pid + "' - " + Thread.currentThread().getName() + " - " + System.currentTimeMillis());
    }

    @Bean
    WaitOnShutdown waitOnShutdown() {
        return new WaitOnShutdown();
    }
}
