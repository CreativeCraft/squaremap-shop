package org.creativecraft.squaremapshop.hook;

import org.bukkit.World;
import org.creativecraft.squaremapshop.SquaremapShop;
import org.creativecraft.squaremapshop.task.SquaremapTask;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SquaremapHook {
    private final Map<UUID, SquaremapTask> provider = new HashMap<>();
    private final SquaremapShop plugin;

    public SquaremapHook(SquaremapShop plugin) {
        this.plugin = plugin;
        hook();
    }

    /**
     * Hook into squaremap.
     */
    public void hook() {
        SquaremapProvider.get().mapWorlds().forEach(value -> {
            final World world = BukkitAdapter.bukkitWorld(value);

            SimpleLayerProvider provider = SimpleLayerProvider
                .builder(plugin.getSettings().getConfig().getString("settings.control.label", "Shops"))
                .showControls(plugin.getSettings().getConfig().getBoolean("settings.control.show"))
                .defaultHidden(plugin.getSettings().getConfig().getBoolean("settings.control.hide"))
                .build();

            value.layerRegistry().register(Key.of("shop_" + world.getUID()), provider);

            SquaremapTask task = new SquaremapTask(plugin, provider);

            task.runTaskTimerAsynchronously(plugin, 20L, 20L * plugin.getSettings().getConfig().getInt("settings.update-interval"));

            this.provider.put(world.getUID(), task);
        });
    }

    /**
     * Disable the squaremap hook.
     */
    public void disable() {
        provider.values().forEach(SquaremapTask::disable);
        provider.clear();

        SquaremapProvider.get().iconRegistry().unregister(
            plugin.getIcons().getIcon()
        );
    }
}
