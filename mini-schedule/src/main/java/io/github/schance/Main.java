package io.github.schance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
        ScheduleService scheduleService = new ScheduleService();
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(formatter) + " 100ms");
        }, 100);

        TimeUnit.SECONDS.sleep(1);

        System.out.println("another task print 2 every 200 milliseconds");
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(formatter) + " 200ms");
        }, 200);
    }
}