package org.creativecraft.squaremapshop.hook;

import com.snowgears.shop.handler.ShopHandler;
import com.snowgears.shop.shop.AbstractShop;
import org.bukkit.Location;
import org.creativecraft.squaremapshop.SquaremapShop;

import java.util.LinkedHashMap;

public class ShopHook {
    private final SquaremapShop plugin;

    public ShopHook(SquaremapShop plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieve the shops.
     *
     * @return LinkedHashMap
     */
    public LinkedHashMap<Location, AbstractShop> getShops() {
        ShopHandler handler = plugin.getShop().getShopHandler();
        LinkedHashMap<Location, AbstractShop> shops = new LinkedHashMap<>();

        handler.getShopOwners().forEach(player -> {
            handler.getShops(player.getUniqueId()).forEach(shop -> {
                if (!shop.isInitialized() || shop.getChestLocation() == null) {
                    return;
                }

                shops.put(shop.getChestLocation(), shop);
            });
        });

        return shops;
    }
}
