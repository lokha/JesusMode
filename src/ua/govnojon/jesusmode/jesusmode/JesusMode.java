package ua.govnojon.jesusmode.jesusmode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import ua.govnojon.jesusmode.JesusModePlugin;

import java.util.ArrayList;

public class JesusMode {

    private JesusModePlugin jesusModePlugin = JesusModePlugin.getInstance();

    private ArrayList<Block> blocks = new ArrayList<>();
    private int limit;
    private Material material;
    private Player player;


    public JesusMode(Player player) {
        this.player = player;
        this.limit = jesusModePlugin.getDefaultLimit();
        this.material = jesusModePlugin.getDelaultFrom();
    }

    public void newPosition(Location location) {
        Location spawn = location.clone();
        spawn.setY(spawn.getBlockY() - (player.getLocation().getPitch() > 70 ? 2 : 1));

        World world = location.getWorld();
        int x = spawn.getBlockX();
        int y = spawn.getBlockY();
        int z = spawn.getBlockZ();

        this.paste(world.getBlockAt(x, y, z));

        this.paste(world.getBlockAt(x + 1, y, z));
        this.paste(world.getBlockAt(x - 1, y, z));

        this.paste(world.getBlockAt(x, y, z + 1));
        this.paste(world.getBlockAt(x, y, z - 1));
    }

    private void paste(Block block) {
        if (block.getType().equals(this.jesusModePlugin.getDelaultTo())) {
            block.setType(material);
            this.addBlock(block);
        } else {
            Material type = block.getType();
            for (SpecialBlock specialBlock : this.jesusModePlugin.getSpecialBlocks()) {
                if (type.equals(specialBlock.getTo())) {
                    block.setType(specialBlock.getFrom());
                    this.addBlock(block);
                    break;
                }
            }
        }
    }

    private void addBlock(Block block) {
        blocks.add(block);
        this.jesusModePlugin.getFakeBlocks().add(block);
    }

    public void removeBlocks() {
        while (blocks.size() > limit) {
            Block block = blocks.remove(0);
            this.removeBlock(block);
        }
    }

    public void removeBlockAll() {
        for (Block block : blocks) {
            this.removeBlock(block);
        }
        blocks.clear();
    }

    private void removeBlock(Block block) {
        this.jesusModePlugin.getFakeBlocks().remove(block);
        Material type = block.getType();
        if (type.equals(material)) {
            block.setType(jesusModePlugin.getDelaultTo());
        } else {
            for (SpecialBlock specialBlock : this.jesusModePlugin.getSpecialBlocks()) {
                if (specialBlock.getFrom().equals(type)) {
                    block.setType(specialBlock.getTo());
                    break;
                }
            }
        }
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.removeBlockAll();
        this.material = material;
    }
}
