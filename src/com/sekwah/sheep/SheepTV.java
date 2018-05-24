package com.sekwah.sheep;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SheepTV extends JavaPlugin {

    public SheepScreen screen;

    @Override
    public void onEnable() {

        this.screen = new SheepScreen(this);

        this.getCommand("sheep").setExecutor(this);

        this.getServer().getScheduler().runTaskTimer(this, new ScreenUpdate(), 0L, 1L);

    }

    @Override
    public void onDisable() {
        screen.destroyScreen();
    }

    class ScreenUpdate extends BukkitRunnable {

        @Override
        public void run() {
            if(screen.isMade()) {
                screen.updateScreen();
            }
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        Player player = (Player) sender;
        if(args.length > 0) {
            switch(args[0]) {
                case "create":
                    if(!screen.isMade()) {
                        screen.createScreen(player.getLocation());
                        sender.sendMessage("Created Screen");
                    }
                    else {
                        sender.sendMessage("Screen already exists");
                    }
                    break;
                case "remove":
                    if(screen.isMade()) {
                        screen.destroyScreen();
                        sender.sendMessage("Screen removed");
                    }
                    else {
                        sender.sendMessage("No screen exists");
                    }
                    break;
                default:
                    sender.sendMessage("Not recognised");
                    break;
            }
        }
        return true;
    }

}
