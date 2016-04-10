package ua.govnojon.jesusmode.jesusmode;

import org.bukkit.Material;

public class SpecialBlock {
    private Material to;
    private Material from;


    public SpecialBlock(Material to, Material from) {
        this.to = to;
        this.from = from;
    }

    public Material getTo() {
        return to;
    }

    public void setTo(Material to) {
        this.to = to;
    }

    public Material getFrom() {
        return from;
    }

    public void setFrom(Material from) {
        this.from = from;
    }
}
