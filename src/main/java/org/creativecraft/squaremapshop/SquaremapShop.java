package org.creativecraft.squaremapshop;

import com.snowgears.shop.Shop;
import org.bstats.bukkit.MetricsLite;
import org.creativecraft.squaremapshop.config.SettingsConfig;
import org.creativecraft.squaremapshop.data.Icons;
import org.creativecraft.squaremapshop.hook.ShopHook;
import org.creativecraft.squaremapshop.hook.SquaremapHook;
import org.bukkit.plugin.java.JavaPlugin;

public class SquaremapShop extends JavaPlugin {
    public static SquaremapShop plugin;
    private SettingsConfig settingsConfig;
    private SquaremapHook squaremapHook;
    private Shop shop;
    private ShopHook shopHook;
    private Icons icons;

    @Override
    public void onEnable() {
        plugin = this;
        shop = Shop.getPlugin();

        registerSettings();
        registerData();
        registerHooks();

        new MetricsLite(this, 16164);
    }

    @Override
    public void onDisable() {
        if (squaremapHook != null) {
            squaremapHook.disable();
        }
    }

    /**
     * Register the plugin hooks.
     */
    public void registerHooks() {
        squaremapHook = new SquaremapHook(this);
        shopHook = new ShopHook(this);
    }

    /**
     * Register the plugin config.
     */
    public void registerSettings() {
        settingsConfig = new SettingsConfig(this);
    }

    /**
     * Register the plugin data.
     */
    public void registerData() {
        icons = new Icons(this);
    }

    /**
     * Retrieve the plugin config.
     *
     * @return Config
     */
    public SettingsConfig getSettings() {
        return settingsConfig;
    }

    /**
     * Retrieve the Shop instance.
     *
     * @return Shop
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * Retrieve the Shop hook instance.
     *
     * @return ShopHook
     */
    public ShopHook getShopHook() {
        return shopHook;
    }

    /**
     * Retrieve the Icons instance.
     *
     * @return Icons
     */
    public Icons getIcons() {
        return icons;
    }
}
