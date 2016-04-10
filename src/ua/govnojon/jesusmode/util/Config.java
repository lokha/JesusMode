package ua.govnojon.jesusmode.util;
import org.apache.commons.io.output.WriterOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;

public class Config extends YamlConfiguration {

    public static final String SEP = File.separator; // separator - длинное слово!!
    private     String            path;
    private     Plugin            plugin;
    private boolean first = false;


    /**
     *
     * @param plugin плагин
     * @param path путь, относительно директории плагина
     */
    public Config(Plugin plugin, String path) {
        this.plugin = plugin;
        this.path = path;
        this.reload();
    }

    public void save() {
        try {
            this.save(plugin.getDataFolder() + SEP + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        plugin.getDataFolder().mkdir();
        File file = new File(plugin.getDataFolder() + SEP + path);

        if (!file.exists()) {
            try {
                first = true;
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCfg() {
        return this;
    }

    /**
     * Установить сначение, если его не существует
     * @param path
     * @param value
     */
    public void setIfNotExist(String path, Object value) {
        if (!this.contains(path)) {
            this.setAndSave(path, value);
        }
    }

    public void setDefault(String path, Object value) {
        if (first) {
            this.setAndSave(path, value);
        }
    }

    /**
     * Редактировать и сохранить конфиг
     * @param path
     * @param value
     */
    public void setAndSave(String path, Object value) {
        this.set(path, value);
        this.save();
    }

    @Override
    public void set(String path, Object value) {
        if (value instanceof Location) {
            super.set(path, toString((Location) value));
        } else if (value instanceof ItemStack) {
            super.set(path, toString((ItemStack) value));
        } else if (value instanceof Material) {
            super.set(path, ((Material)value).name());
        } else {
            super.set(path, value);
        }
    }

    public static String toString(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        String code = item.getType().name().toLowerCase()
                + ":" + item.getDurability()
                + ":" + item.getAmount()
                + ":" + (meta.getDisplayName() == null ? "none" : meta.getDisplayName().replace(":", "<<>>"))
                + ":";
        boolean first = true;
        if (meta.getLore() != null) {
            for (String lore : meta.getLore()) {
                if (first) {
                    first = false;
                } else {
                    code += "\\n";
                }
                code += lore.replace(":", "<<>>");
            }
        } else {
            code += "none";
        }
        code += ":";
        first = true;
        if (meta.getEnchants().size() != 0) {
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                if (first) {
                    first = false;
                } else {
                    code += ",";
                }
                code += enchantment.getName().toLowerCase() + "*" + meta.getEnchantLevel(enchantment);
            }
        } else {
            code += "none";
        }
        return code;
    }

    public String getStringColor(String path) {
        return StringUtil.replace(this.getString(path), "&", "§");
    }

    public Location getLocation(String path) {
        String code = this.getString(path);
        return toLocation(code);
    }

    public ItemStack getItemStack(String path) {
        String code = this.getString(path);
        return toItemStack(code);
    }

    public static ItemStack toItemStack(String code) {
        if (code == null || code.length() == 0)
            return null;

        String item_s[] = code.split(":");
        if (item_s.length != 6)
            return null;
        ItemStack item = new ItemStack(
                toMaterial(item_s[0]),
                Integer.parseInt(item_s[2]),
                Short.parseShort(item_s[1])
        );
        ItemMeta meta = item.getItemMeta();
        if (!item_s[3].equals("none"))
            meta.setDisplayName(item_s[3].replace("<<>>", ":"));
        if (!item_s[4].equals("none")) {
            ArrayList<String> lores = new ArrayList<>();
            for (String lore : item_s[4].split("\\n")) {
                lores.add(lore);
            }
            meta.setLore(lores);
        }
        if (!item_s[5].equals("none")) {
            for (String enchant : item_s[5].split(",")) {
                String sett[] = enchant.split("\\*");
                meta.addEnchant(Enchantment.getByName(sett[0].toUpperCase()), Integer.parseInt(sett[1]), false);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String toString(Location loc) {
        return loc.getWorld().getName()
                + ":" + loc.getX()
                + ":" + loc.getY()
                + ":" + loc.getZ()
                + ":" + loc.getYaw()
                + ":" + loc.getPitch();
    }

    public static Location toLocation(String code) {
        if (code == null || code.equals(""))
            return null;

        String loc[] = code.split(":");
        if (loc.length != 6)
            return null;

        return new Location(
                Bukkit.getWorld(loc[0]),
                Double.parseDouble(loc[1]),
                Double.parseDouble(loc[2]),
                Double.parseDouble(loc[3]),
                Float.parseFloat(loc[4]),
                Float.parseFloat(loc[5])
        );
    }

    public void createSectionIfNotExist(String path) {
        //я даун
        if (!this.contains(path)) {
            this.setAndSave(path + ".lkjsdafknhsdjklhfalsjhfwui3324ij2i3j4", 10);
            this.setAndSave(path + ".lkjsdafknhsdjklhfalsjhfwui3324ij2i3j4", null);
        }
    }

    public Material getMaterial(String start) {
        String mat = this.getString(start);
        if (mat == null) {
            return null;
        }

        return toMaterial(mat);
    }

    public static Material toMaterial(String code) {
        try {
            return Material.getMaterial(Integer.parseInt(code));
        } catch (Exception e) {
            return Material.valueOf(code.toUpperCase());
        }
    }
}
