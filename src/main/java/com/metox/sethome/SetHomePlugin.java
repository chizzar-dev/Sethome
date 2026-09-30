package com.metox.sethome;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SetHomePlugin extends JavaPlugin implements Listener, TabExecutor {

    private Warmups warmups;
    private HomeStore store;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        store = new HomeStore(this);
        warmups = new Warmups(this, "sethome.bypass");

        String[] cmds = {"sethome", "home", "delhome", "homes"};
        for (String cmd : cmds) {
            if (getCommand(cmd) != null) {
                getCommand(cmd).setExecutor(this);
                getCommand(cmd).setTabCompleter(this);
            }
        }
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("SetHome aktif - algilanan surum 1." + Compat.MINOR);
        // chizzar-dev
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            send(sender, "players-only");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("sethome.use")) {
            send(p, "no-permission");
            return true;
        }

        String name = command.getName().toLowerCase();

        if (name.equals("sethome")) return doSet(p, args);
        if (name.equals("home")) return doHome(p, args);
        if (name.equals("delhome")) return doDel(p, args);
        if (name.equals("homes")) return doList(p);
        return true;
    }

    // chizzar-dev
    private boolean doSet(Player p, String[] args) {
        String name = resolveName(args);
        if (name == null) {
            send(p, "invalid-name");
            return true;
        }

        boolean existed = store.has(p.getUniqueId(), name);
        if (!existed && !p.hasPermission("sethome.unlimited")) {
            int max = getConfig().getInt("max-homes", 3);
            if (store.count(p.getUniqueId()) >= max) {
                send(p, "limit-reached", "%max%", String.valueOf(max));
                return true;
            }
        }

        store.set(p.getUniqueId(), name, p.getLocation());
        send(p, existed ? "overwritten" : "set", "%home%", name);
        return true;
    }

    private boolean doHome(Player p, String[] args) {
        String name = resolveName(args);
        if (name == null) {
            send(p, "invalid-name");
            return true;
        }

        Location loc = store.get(p.getUniqueId(), name);
        if (loc == null) {
            send(p, "home-not-found", "%home%", name);
            return true;
        }

        warmups.start(p, loc);
        return true;
    }

    private boolean doDel(Player p, String[] args) {
        String name = resolveName(args);
        if (name == null) {
            send(p, "invalid-name");
            return true;
        }
        if (!store.has(p.getUniqueId(), name)) {
            send(p, "home-not-found", "%home%", name);
            return true;
        }
        store.delete(p.getUniqueId(), name);
        send(p, "deleted", "%home%", name);
        return true;
    }

    private boolean doList(Player p) {
        java.util.Set<String> names = store.names(p.getUniqueId());
        if (names.isEmpty()) {
            send(p, "no-homes");
            return true;
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String n : names) {
            if (!first) sb.append("&7, &f");
            sb.append(n);
            first = false;
        }
        send(p, "list", "%homes%", sb.toString());
        return true;
    }


    private String resolveName(String[] args) {
        String raw = args.length >= 1 ? args[0] : getConfig().getString("default-home", "home");
        if (raw == null) return null;
        raw = raw.toLowerCase();
        if (!raw.matches("[a-z0-9_-]{1,32}")) return null;
        return raw;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo() == null) return;
        warmups.onMove(e.getPlayer(), e.getFrom(), e.getTo());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        warmups.cancelSilent(e.getPlayer());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> out = new ArrayList<String>();
        if (args.length != 1 || !(sender instanceof Player)) return out;

        String name = command.getName().toLowerCase();
        if (name.equals("home") || name.equals("delhome")) {
            String pref = args[0].toLowerCase();
            for (String n : store.names(((Player) sender).getUniqueId())) {
                if (n.toLowerCase().startsWith(pref)) out.add(n);
            }
        }
        return out;
    }

    public void send(CommandSender sender, String key, String... repl) {
        String m = getConfig().getString("messages." + key, "");
        if (m == null || m.isEmpty()) return;
        for (int i = 0; i + 1 < repl.length; i += 2) {
            m = m.replace(repl[i], repl[i + 1]);
        }
        sender.sendMessage(Compat.color(m));
    }
}
