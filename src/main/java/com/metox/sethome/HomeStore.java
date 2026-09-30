package com.metox.sethome;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class HomeStore {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration cfg;

    public HomeStore(JavaPlugin plugin) {
        this.plugin = plugin;
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        this.file = new File(plugin.getDataFolder(), "homes.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    private String base(UUID u, String name) {
        return "players." + u + "." + name;
    }

    public boolean has(UUID u, String name) {
        return cfg.contains(base(u, name) + ".world");
    }

    // chizzar-dev
    public void set(UUID u, String name, Location loc) {
        String b = base(u, name);
        cfg.set(b + ".world", loc.getWorld().getName());
        cfg.set(b + ".x", loc.getX());
        cfg.set(b + ".y", loc.getY());
        cfg.set(b + ".z", loc.getZ());
        cfg.set(b + ".yaw", (double) loc.getYaw());
        cfg.set(b + ".pitch", (double) loc.getPitch());
        save();
    }

    public Location get(UUID u, String name) {
        String b = base(u, name);
        String w = cfg.getString(b + ".world");
        if (w == null) return null;

        World world = Bukkit.getWorld(w);
        if (world == null) return null;

        double x = cfg.getDouble(b + ".x");
        double y = cfg.getDouble(b + ".y");
        double z = cfg.getDouble(b + ".z");
        float yaw = (float) cfg.getDouble(b + ".yaw");
        float pitch = (float) cfg.getDouble(b + ".pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public void delete(UUID u, String name) {
        cfg.set(base(u, name), null);
        save();
    }

    public Set<String> names(UUID u) {
        ConfigurationSection sec = cfg.getConfigurationSection("players." + u);
        if (sec == null) return new LinkedHashSet<String>();
        return sec.getKeys(false);
    }

    public int count(UUID u) {
        return names(u).size();
    }

    private void save() {
        try {
            cfg.save(file);
        } catch (IOException ex) {
            plugin.getLogger().warning("homes.yml kaydedilemedi: " + ex.getMessage());
        }
    }
}
