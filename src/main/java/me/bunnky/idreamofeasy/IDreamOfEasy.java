package me.bunnky.idreamofeasy;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import me.bunnky.idreamofeasy.listeners.IdolListener;
import me.bunnky.idreamofeasy.listeners.MagnetoidListener;
import me.bunnky.idreamofeasy.slimefun.setup.Setup;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.text.MessageFormat;

public class IDreamOfEasy extends JavaPlugin implements SlimefunAddon {
    private static IDreamOfEasy instance;

    private static final String REPOSITORY_OWNER = "wickidcow";
    private static final String REPOSITORY_NAME = "IDreamOfEasy";

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info(" ┳  ┳┓┳┓┏┓┏┓┳┳┓  ┏┓┏┓  ┏┓┏┓┏┓┓┏ ");
        getLogger().info(" ┃  ┃┃┣┫┣ ┣┫┃┃┃  ┃┃┣   ┣ ┣┫┗┓┗┫ ");
        getLogger().info(" ┻  ┻┛┛┗┗┛┛┗┛ ┗  ┗┛┻   ┗┛┛┗┗┛┗┛ ");
        getLogger().info("        IDOE by Bunnky          ");
        getLogger().info(" Slimefun Legacy maintenance build for Paper 26.2 ");

        saveDefaultConfig();
        setupMetrics();

        Setup.setup();

        new MagnetoidListener(this);
        new IdolListener(this);
    }

    private void setupMetrics() {
        new Metrics(this, 23610);
    }

    public static void consoleMsg(@Nonnull String string) {
        instance.getLogger().info(string);
    }

    public static IDreamOfEasy getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return MessageFormat.format("https://github.com/{0}/{1}/issues", REPOSITORY_OWNER, REPOSITORY_NAME);
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
