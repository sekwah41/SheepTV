package com.sekwah.sheep;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sound.sampled.*;

public class SheepTV extends JavaPlugin {

    public SheepScreen screen;

    public static byte[] data;
    public static int numBytesRead;
    private Thread thread;
    private TargetDataLine audioIn;

    @Override
    public void onEnable() {

        this.screen = new SheepScreen(this);

        this.getCommand("sheep").setExecutor(this);

        this.getServer().getScheduler().runTaskTimer(this, new ScreenUpdate(), 0L, 1L);

        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            audioIn = (TargetDataLine) AudioSystem.getLine(info);
            audioIn.open(format);

            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            //int numBytesRead;
            int CHUNK_SIZE = 1024;
            data = new byte[1024];
            System.out.println(data.length);
            audioIn.start();


            Runnable runnable = () -> {
                while (true) {
                    //updates++;
                    numBytesRead = audioIn.read(data, 0, CHUNK_SIZE);
                    //int latest = 0;
                    //int time = 0;
                    //System.out.println(updates);
                    // write the mic data to a stream for use later
                    // out.write(data, 0, numBytesRead);
                }
            };

            thread = new Thread(runnable);
            thread.start();
            //audioIn.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        thread.stop();
        audioIn.close();
        screen.destroyScreen();
    }

    class ScreenUpdate extends BukkitRunnable {

        @Override
        public void run() {
            if(screen.isMade()) {
                float average = 0;
                for(int i = 0; i < numBytesRead; i++) {
                    //System.out.println(data[i]);
                    if(data[i] < 0){
                        average -= data[i];
                    }
                    else{
                        average += data[i];
                    }
                }
                average /= (float) numBytesRead;

                average -= 25;
                average /= 20;
                if(average > 1){
                    average = 1;
                }
                else if(average < 0){
                    average = 0;
                }
                screen.setColorLevel(average);
                /*screen.updateScreen();*/
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
