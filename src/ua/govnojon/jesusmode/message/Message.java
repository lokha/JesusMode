package ua.govnojon.jesusmode.message;

import ua.govnojon.jesusmode.JesusModePlugin;
import ua.govnojon.jesusmode.util.Config;

public enum Message {

    HELP("help",
            "&e==========[JesusMode]==========\n" +
                    "&4/jesusmode on/off &7- on or off jesus mode\n" +
                    "&4/jesusmode setlimit [limit] &7- set limit blocks\n" +
                    "&4/jesusmode setmaterial [material] &7- set material blocks"
    ),
    NO_PERM("no-perm", "&cNo permission."),
    ALREADY_INCLUDED("already-included", "&cJesus mode already included."),
    NOT_ENABLED("not-enabled", "&cJesus mode is not enabled."),
    JESUS_ENABLE("jesus-enable", "&aJesus mode enabled."),
    JESUS_DISABLE("jesus-disable", "&aJesus mode disabled."),
    FORMAT_SETLIMIT("format-setlimit", "&7/jesusmode setlimit [limit]"),
    FORMAT_SETMATERIAL("format-setlimit", "&7/jesusmode setmaterial [material]"),
    BED_VALUE("bed-value", "&cBad value '%value%'."),
    SET_LIMIT("set-limit", "&aLimit saved."),
    SET_MATERIAL("set-material", "&aMaterial is installed."),
    ARGUMENT_NOT_FOUND("argument-not-found", "&cAgument not found."),
    NOT_ALLOWED_MATERIAL("not-allowed-material", "&cThis material can not be used."),
    LIMIT_DIAPAZONE("limit-diapazone", "&cEnter value from %from% to %to%.")
    ;

    private String key;
    private String defauld;
    private String message;

    Message(String key, String defauld) {
        this.key = key;
        this.defauld = defauld;
        this.load();
    }

    public void load() {
        JesusModePlugin.getInstance().getConfig().setIfNotExist("messages." + key, defauld);
        this.message = JesusModePlugin.getInstance().getConfig().getStringColor("messages." + key);
    }

    public String getMessage() {
        return message;
    }
}