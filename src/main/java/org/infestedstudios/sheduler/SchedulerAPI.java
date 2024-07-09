package org.infestedstudios.sheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.infestedstudios.sheduler.annotations.AnnotationProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class SchedulerAPI {
    private static JavaPlugin plugin;
    private static ExecutorService asyncExecutor;
    private static Map<String, Integer> taskIds = new HashMap<>();

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
        asyncExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        new AnnotationProcessor(plugin);
    }

    public static void shutdown() {
        asyncExecutor.shutdown();
        try {
            if (!asyncExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            asyncExecutor.shutdownNow();
        }
    }

    public static void scheduleSyncTask(String id, Runnable task, long delay) {
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
        taskIds.put(id, taskId);
    }

    public static void scheduleAsyncTask(String id, Runnable task) {
        asyncExecutor.submit(task);
        taskIds.put(id, -1); // Async tasks don't have a Bukkit task ID
    }

    public static void cancelTask(String id) {
        if (taskIds.containsKey(id)) {
            int taskId = taskIds.get(id);
            if (taskId != -1) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
            taskIds.remove(id);
        }
    }
}