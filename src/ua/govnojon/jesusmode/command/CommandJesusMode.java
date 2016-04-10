package ua.govnojon.jesusmode.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.govnojon.jesusmode.JesusModePlugin;
import ua.govnojon.jesusmode.jesusmode.JesusMode;
import ua.govnojon.jesusmode.message.Message;
import ua.govnojon.jesusmode.util.Config;
import ua.govnojon.jesusmode.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandJesusMode extends Command {

    private ArrayList<String> argssss = new ArrayList<>();

    private JesusModePlugin jesusModePlugin = JesusModePlugin.getInstance();

    public CommandJesusMode(String name) {
        super(name);

        argssss.add("on");
        argssss.add("off");
        argssss.add("setlimit");
        argssss.add("setmaterial");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String zzzzzzzzzzz, String[] args) throws IllegalArgumentException {
        if (sender instanceof Player) {
            if (sender.hasPermission(jesusModePlugin.getPerm())) {
                if (args.length == 1) {
                    ArrayList<String> tab = new ArrayList<>();
                    for (String ar : argssss) {
                        if (StringUtil.containsIgnoreCase(ar, args[0])) {
                            tab.add(ar);
                        }
                    }
                    if (sender.hasPermission(jesusModePlugin.getPermAdmin())) {
                        tab.add("reload");
                    }
                    return tab;
                }

                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("setmaterial")) {
                        ArrayList<String> tab = new ArrayList<>();
                        for (Material ar : jesusModePlugin.getAllowedMaterial()) {
                            if (StringUtil.containsIgnoreCase(ar.name(), args[1])) {
                                tab.add(ar.name());
                            }
                        }
                        return tab;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, String zzzzzzzzzzzzzz, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only player.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(jesusModePlugin.getPerm())) {
            player.sendMessage(Message.NO_PERM.getMessage());
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Message.HELP.getMessage());
            return true;
        }

        args[0] = args[0].toLowerCase();
        JesusMode jesusMode = jesusModePlugin.getJesusMode().get(player);


        if (args[0].equals("on")) {
            if (jesusMode != null) {
                player.sendMessage(Message.ALREADY_INCLUDED.getMessage());
                return false;
            }
            this.jesusModePlugin.getJesusMode().put(player, new JesusMode(player));
            player.sendMessage(Message.JESUS_ENABLE.getMessage());
            return true;
        }

        if (args[0].equals("off")) {
            if (jesusMode == null) {
                player.sendMessage(Message.NOT_ENABLED.getMessage());
                return false;
            }
            this.jesusModePlugin.getJesusMode().remove(player);
            player.sendMessage(Message.JESUS_DISABLE.getMessage());
            return true;
        }

        if (args[0].equals("setlimit")) {
            if (jesusMode == null) {
                player.sendMessage(Message.NOT_ENABLED.getMessage());
                return false;
            }
            if (args.length < 2) {
                player.sendMessage(Message.FORMAT_SETLIMIT.getMessage());
                return false;
            }
            Integer limit = null;
            try {
                limit = Integer.parseInt(args[1]);
            } catch (Exception e) {
                player.sendMessage(Message.BED_VALUE.getMessage().replace("%value%", args[1]));
                return false;
            }
            if (limit > jesusModePlugin.getMaxLimit() || limit < jesusModePlugin.getMinLimit()) {
                player.sendMessage(Message.LIMIT_DIAPAZONE.getMessage()
                        .replace("%from%", String.valueOf(jesusModePlugin.getMinLimit()))
                        .replace("%to%", String.valueOf(jesusModePlugin.getMaxLimit())));
                return false;
            }

            jesusMode.removeBlockAll();
            jesusMode.setLimit(limit);
            player.sendMessage(Message.SET_LIMIT.getMessage());
            return true;
        }

        if (args[0].equals("setmaterial")) {
            if (jesusMode == null) {
                player.sendMessage(Message.NOT_ENABLED.getMessage());
                return false;
            }
            if (args.length < 2) {
                player.sendMessage(Message.FORMAT_SETMATERIAL.getMessage());
                return false;
            }
            Material material = null;
            try {
                material = Config.toMaterial(args[1]);
            } catch (Exception e) {
                player.sendMessage(Message.BED_VALUE.getMessage().replace("%value%", args[1]));
                return false;
            }
            if (!this.jesusModePlugin.getAllowedMaterial().contains(material)) {
                player.sendMessage(Message.NOT_ALLOWED_MATERIAL.getMessage());
                return false;
            }

            jesusMode.setMaterial(material);
            jesusMode.removeBlockAll();
            player.sendMessage(Message.SET_MATERIAL.getMessage());
            return true;
        }

        if (args[0].equals("reload")) {
            if (!player.hasPermission(jesusModePlugin.getPermAdmin())) {
                player.sendMessage(Message.NO_PERM.getMessage());
                return false;
            }
            jesusModePlugin.onEnable();
            for (JesusMode jesus : jesusModePlugin.getJesusMode().values()) {
                jesus.removeBlockAll();
            }
            sender.sendMessage("§aConfig reloaded.");
            return true;
        }

        sender.sendMessage(Message.ARGUMENT_NOT_FOUND.getMessage());
        return false;
    }
}
