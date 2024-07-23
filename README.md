![image](https://github.com/InfestedStudios/SchedulerAPI/assets/42579059/791b537b-e736-45b1-acac-f1583fd48d50)

# SchedulerAPI

SchedulerAPI is a robust and flexible task-scheduling library for Minecraft plugins. It simplifies the process of scheduling synchronous and asynchronous tasks,
recurring tasks, and event-driven tasks. It also provides features like task prioritization, dependencies, retries, and task grouping. The API is designed to 
integrate seamlessly with existing setups, making managing tasks in Minecraft plugins easy.

## Features

- **Annotation Support** Use @ScheduleTask() and look at examples for further understanding.
- **Synchronous and Asynchronous Tasks:** Schedule tasks to run on the main thread or asynchronously.
- **Recurring Tasks:** Schedule tasks to run at fixed intervals.
- **Task Prioritization:** Assign priorities to tasks for ordered execution.
- **Task Dependencies:** Ensure tasks run after other specified tasks are complete.
- **Task Groups:** Group tasks for batch operations.
- **Retry Mechanism:** Automatic retries for failed tasks.
- **Event-Driven Tasks:** Trigger tasks based on specific events.
- **Task ID Management:** Easily integrate with preexisting setups.
- **Completion Callbacks:** Provide feedback upon task completion.
- **Task Result Handling:** Support for tasks that return results.


## Usage
```Java
package com.example.myplugin;

import com.example.schedulerapi.SchedulerAPI;
import com.example.schedulerapi.annotations.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TaskHandler implements Listener {

    public TaskHandler(JavaPlugin plugin) {
        // Register event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Scheduled recurring async task for announcements
    @ScheduledTask(id = "announcementTask", async = true, delay = "10S", interval = "5M")
    public void makeAnnouncements() {
        Bukkit.getLogger().info("Announcement: Remember to check our website for updates!");
    }

    // Scheduled sync task to reward players
    @ScheduledTask(id = "rewardTask", async = false, delay = "10S")
    public void rewardPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("You have been rewarded for playing on our server!");
            player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
        }
    }

    // Handle block break event and reward player
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        if (blockType == Material.DIAMOND_ORE) {
            SchedulerAPI.scheduleSyncTask("blockBreakRewardTask", () -> {
                player.sendMessage("You broke a diamond ore! Here is an extra reward.");
                player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
            }, 0);
        }
    }
}

```
  
