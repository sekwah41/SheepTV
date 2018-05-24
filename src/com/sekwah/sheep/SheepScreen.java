package com.sekwah.sheep;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Sheep;

public class SheepScreen {

    private final SheepTV plugin;

    private Sheep[][] pixels;

    private final double offsetX = 11.2f/16f;
    private final double offsetZ = 21.2f/16f;

    private final int sizeX = 30;
    private final int sizeZ = 30;

    private boolean isMade = false;

    private int updateCount = 0;

    public SheepScreen(SheepTV plugin) {
        this.plugin = plugin;
        this.pixels = new Sheep[sizeX][sizeZ];
    }

    public void createScreen(Location loc) {
        this.isMade = true;
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                loc.setPitch(90);
                loc.setYaw(0);
                Sheep sheep = loc.getWorld().spawn(loc.clone().add(x * offsetX,0,z * offsetZ), Sheep.class);
                sheep.setGravity(false);
                sheep.setAI(false);
                sheep.setCollidable(false);
                sheep.setInvulnerable(true);
                sheep.setColor(DyeColor.WHITE);
                pixels[x][z] = sheep;
            }
        }
    }

    public void destroyScreen() {
        this.isMade = false;
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                pixels[x][z].remove();
            }
        }
    }

    public boolean isMade() {
        return isMade;
    }

    public void updateSheep(int x, int z, DyeColor color) {
        if(pixels[x][z].getColor() != color) {
            pixels[x][z].setColor(color);
        }
    }

    public void updateScreen() {
        updateCount++;
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                int i = ((updateCount + x) + (z / 4) * 4) % 8;
                if(i >= 4) {
                    updateSheep(x,z,DyeColor.BLACK);
                }
                else {

                    updateSheep(x,z,DyeColor.WHITE);
                }
            }
        }
    }
}
