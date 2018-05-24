package com.sekwah.sheep;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class SheepScreen {

    private final SheepTV plugin;

    private ArmorStand[][] pixels;

    private final double offsetX = 7.2f/16f;
    private final double offsetZ = 7.2f/16f;

    private final int sizeX = 25;
    private final int sizeZ = 25;

    private boolean isMade = false;

    private int updateCount = 0;

    public SheepScreen(SheepTV plugin) {
        this.plugin = plugin;
        this.pixels = new ArmorStand[sizeX][sizeZ];
    }

    public void createScreen(Location loc) {
        this.isMade = true;
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                loc.setPitch(90);
                loc.setYaw(0);
                ArmorStand pixel = loc.getWorld().spawn(loc.clone().add(x * offsetX,0,z * offsetZ), ArmorStand.class);
                pixel.setGravity(false);
                pixel.setAI(false);
                pixel.setCollidable(false);
                pixel.setInvulnerable(true);;
                pixel.setHelmet(new Wool(DyeColor.WHITE).toItemStack(1));
                pixels[x][z] = pixel;
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
        /*if(pixels[x][z].getColor() != color) {
            pixels[x][z].setColor(color);
        }*/
        if(pixels[x][z].getHelmet().getData().getData() != color.getWoolData()) {
            pixels[x][z].setHelmet(new Wool(color).toItemStack(1));
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
