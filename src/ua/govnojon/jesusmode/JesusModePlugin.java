package ua.govnojon.jesusmode;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ua.govnojon.jesusmode.command.CommandJesusMode;
import ua.govnojon.jesusmode.command.CommandManager;
import ua.govnojon.jesusmode.jesusmode.JesusMode;
import ua.govnojon.jesusmode.jesusmode.SpecialBlock;
import ua.govnojon.jesusmode.listeners.Listeners;
import ua.govnojon.jesusmode.message.Message;
import ua.govnojon.jesusmode.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JesusModePlugin extends JavaPlugin {

    private static JesusModePlugin instance;
    public static JesusModePlugin getInstance() {
        return instance;
    }

    private List<String> defaultAllowedMaterials = Arrays.asList(
            Material.SNOW_BLOCK.name(),
            Material.CLAY.name(),
            Material.PUMPKIN.name(),
            Material.SMOOTH_BRICK.name(),
            Material.NETHER_BRICK.name(),
            Material.QUARTZ_BLOCK.name(),
            Material.STAINED_CLAY.name(),
            Material.PRISMARINE.name(),
            Material.PACKED_ICE.name(),
            Material.DIRT.name(),
            Material.GRASS.name(),
            Material.STONE.name(),
            Material.WOOD.name(),
            Material.COBBLESTONE.name(),
            Material.WOOL.name(),
            Material.SANDSTONE.name(),
            Material.LOG.name(),
            Material.GLASS.name(),
            Material.GRAVEL.name(),
            Material.SAND.name()
            );

    //system
    private Config config = new Config(this, "config.yml");
    private ArrayList<Block> fakeBlocks = new ArrayList<>();
    private HashMap<Player, JesusMode> jesusMode = new HashMap<>();

    //plugin
    private ArrayList<SpecialBlock> specialBlocks = new ArrayList<>();
    private String command;
    private String perm;
    private String permAdmin;
    private ArrayList<Material> allowedMaterial = new ArrayList<>();
    private Material delaultTo = Material.AIR;
    private Material delaultFrom = Material.STONE;
    private int defaultLimit = 10;
    private int maxLimit= 500;
    private int minLimit= 5;

    private boolean first = true;

    @Override
    public void onEnable() {
        instance = this;
        this.config.reload();

        config.setIfNotExist("default-to", "AIR");
        this.delaultTo = config.getMaterial("default-to");
        if (delaultTo == null) {
            delaultTo = Material.AIR;
            this.getLogger().severe("Bed value config 'default-to'.");
        }

        config.setIfNotExist("default-from", "STONE");
        this.delaultFrom = config.getMaterial("default-from");
        if (delaultFrom == null) {
            delaultFrom = Material.STONE;
            this.getLogger().severe("Bed value config 'default-from'.");
        }

        config.setIfNotExist("default-limit", 25);
        this.defaultLimit = config.getInt("default-limit");

        config.setIfNotExist("max-limit", 500);
        this.maxLimit = config.getInt("max-limit");

        config.setIfNotExist("min-limit", 5);
        this.minLimit = config.getInt("min-limit");

        config.createSectionIfNotExist("special-blocks");
        config.setDefault("special-blocks.WATER", "ICE");
        config.setDefault("special-blocks.LAVA", "OBSIDIAN");
        config.setDefault("special-blocks.STATIONARY_WATER", "ICE");
        config.setDefault("special-blocks.STATIONARY_LAVA", "OBSIDIAN");
        this.specialBlocks.clear();
        for (String key : config.getConfigurationSection("special-blocks").getKeys(false)) {
            Material keyM = Config.toMaterial(key);
            Material valueM = config.getMaterial("special-blocks." + key);
            if (key == null || valueM == null) {
                this.getLogger().severe("Bed value cinfig 'special-blocks." + key + "'.");
            } else {
                this.specialBlocks.add(new SpecialBlock(keyM, valueM));
            }
        }


        config.setIfNotExist("allowed-materials", this.defaultAllowedMaterials);
        this.allowedMaterial.clear();
        for (String mat : config.getStringList("allowed-materials")) {
            Material material = Config.toMaterial(mat);
            if (material == null) {
                this.getLogger().severe("Bed value 'allowed-materials[..., '" + mat + "',...]'.");
            } else {
                this.allowedMaterial.add(material);
            }
        }

        config.setIfNotExist("command-name", "jesusmode");
        this.command = config.getString("command-name");

        config.setIfNotExist("perm", "jesus.mode");
        this.perm = config.getString("perm");

        config.setIfNotExist("perm-admin", "jesus.mode.admin");
        this.permAdmin = config.getString("perm-admin");

        for (Message message : Message.values()) {
            message.load();
        }

        if (first) {
            first = false;
            CommandManager.registerCommand(new CommandJesusMode(command));
            Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        }
    }

    @Override
    public void onDisable() {
        for (JesusMode jesusMode : this.jesusMode.values()) {
            jesusMode.removeBlockAll();
        }
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public String getPermAdmin() {
        return permAdmin;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public int getMinLimit() {
        return minLimit;
    }

    public int getDefaultLimit() {
        return defaultLimit;
    }

    public ArrayList<Block> getFakeBlocks() {
        return fakeBlocks;
    }

    public ArrayList<Material> getAllowedMaterial() {
        return allowedMaterial;
    }

    public String getPerm() {
        return perm;
    }

    public HashMap<Player, JesusMode> getJesusMode() {
        return jesusMode;
    }

    public ArrayList<SpecialBlock> getSpecialBlocks() {
        return specialBlocks;
    }

    public Material getDelaultTo() {
        return delaultTo;
    }

    public Material getDelaultFrom() {
        return delaultFrom;
    }

    public String getCommand() {
        return command;
    }
}
