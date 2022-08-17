package org.creativecraft.squaremapshop.task;

import com.snowgears.shop.shop.AbstractShop;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.creativecraft.squaremapshop.SquaremapShop;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Locale;

public class SquaremapTask extends BukkitRunnable {
    private final SimpleLayerProvider provider;
    private final SquaremapShop plugin;

    private boolean stop;

    public SquaremapTask(SquaremapShop plugin, SimpleLayerProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    @Override
    public void run() {
        if (stop) {
            cancel();
        }

        updateShops();
    }

    /**
     * Update the shops.
     */
    private void updateShops() {
        provider.clearMarkers();

        LinkedHashMap<Location, AbstractShop> shops = plugin.getShopHook().getShops();

        if (shops == null || shops.isEmpty()) {
            return;
        }

        shops.forEach(this::handleShop);
    }

    /**
     * Handle the shop on the map.
     *
     * @param shop The shop.
     */
    private void handleShop(Location location, AbstractShop shop) {
        if (location.getWorld() == null) {
            return;
        }

        Icon icon = Icon.icon(
            Point.of(location.getBlockX(), location.getBlockZ()),
            plugin.getIcons().getIcon(),
            plugin.getIcons().getSize()
        );

        ItemMeta itemMeta = shop.getItemStack().getItemMeta();
        String itemName = itemMeta != null && itemMeta.hasDisplayName() ?
            ChatColor.stripColor(itemMeta.getDisplayName()) :
            shop.getItemStack().getType().name();

        MarkerOptions.Builder options = MarkerOptions
            .builder()
            .clickTooltip(
                plugin.getSettings().getConfig().getString("settings.tooltip.shop")
                    .replace("{owner}", shop.getOwnerName())
                    .replace("{type}", StringUtils.capitalize(shop.getType().name().toLowerCase()))
                    .replace("{item}", itemName)
                    .replace("{price}", shop.getPricePerItemString())
                    .replace("{amount}", Integer.toString(shop.getAmount()))
            );

        icon.markerOptions(options);

        String markerId = "shop_" + location.getWorld().getName() + "_" + shop.getOwnerName() + "_" + location.getBlockX() + "_" + location.getBlockZ();

        this.provider.addMarker(Key.of(markerId), icon);
    }

    /**
     * Disable the task.
     */
    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
