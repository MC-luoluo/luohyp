package moe.luoluo.util;

import moe.luoluo.Data;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Task {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Runnable task = Task::executeTask;

    public static void start() {
        executeTask(); // 创建实例时直接执行
    }

    private static void executeTask() {
        try {
            // 在这里编写你的任务逻辑
            Data.resetData();
            System.out.println("luohyp: 缓存已清空");
        } catch (Exception e) {
            System.err.println("任务执行发生异常: " + e.getMessage());
            // 可以添加更详细的日志记录或告警处理
        } finally {
            scheduleNextExecution(); // 任务执行完毕后，重新调度下一次执行
        }
    }

    private static void scheduleNextExecution() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecutionTime = getNextWednesdayMidnight(now);
        long delay = Duration.between(now, nextExecutionTime).toMillis();

        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    private static LocalDateTime getNextWednesdayMidnight(LocalDateTime now) {
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();
        LocalDate today = now.toLocalDate();
        LocalDate nextWednesday;

        if (currentDayOfWeek.getValue() < DayOfWeek.WEDNESDAY.getValue()) {
            nextWednesday = today.with(DayOfWeek.WEDNESDAY);
        } else {
            nextWednesday = today.plusWeeks(1).with(DayOfWeek.WEDNESDAY);
        }
        return nextWednesday.atStartOfDay();
    }
}