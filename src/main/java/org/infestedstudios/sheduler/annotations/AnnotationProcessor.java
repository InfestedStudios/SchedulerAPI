package org.infestedstudios.sheduler.annotations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.infestedstudios.sheduler.SchedulerAPI;

import java.lang.reflect.Method;

public class AnnotationProcessor {

    public AnnotationProcessor(JavaPlugin plugin) {
        processAnnotations(plugin);
    }

    private void processAnnotations(JavaPlugin plugin) {
        Method[] methods = plugin.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ScheduledTask.class)) {
                ScheduledTask annotation = method.getAnnotation(ScheduledTask.class);
                scheduleTask(plugin, method, annotation);
            }
        }
    }

    private void scheduleTask(JavaPlugin plugin, Method method, ScheduledTask annotation) {
        String id = annotation.id();
        boolean async = annotation.async();
        long delay = parseDelay(annotation.delay());
        long interval = annotation.interval().isEmpty() ? -1 : parseDelay(annotation.interval());

        Runnable task = () -> {
            try {
                method.setAccessible(true);
                method.invoke(plugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) {
            SchedulerAPI.scheduleAsyncTask(id, task);
        } else {
            if (interval > 0) {
                int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, interval);
                SchedulerAPI.scheduleSyncTask(id, () -> Bukkit.getScheduler().cancelTask(taskId), 0);
            } else {
                SchedulerAPI.scheduleSyncTask(id, task, delay);
            }
        }
    }

    private long parseDelay(String delay) {
        long ticks = 0;
        if (delay.endsWith("S")) {
            ticks = Long.parseLong(delay.substring(0, delay.length() - 1)) * 20;
        } else if (delay.endsWith("M")) {
            ticks = Long.parseLong(delay.substring(0, delay.length() - 1)) * 20 * 60;
        } else if (delay.endsWith("H")) {
            ticks = Long.parseLong(delay.substring(0, delay.length() - 1)) * 20 * 60 * 60;
        } else if (delay.endsWith("D")) {
            ticks = Long.parseLong(delay.substring(0, delay.length() - 1)) * 20 * 60 * 60 * 24;
        }
        return ticks;
    }
}
