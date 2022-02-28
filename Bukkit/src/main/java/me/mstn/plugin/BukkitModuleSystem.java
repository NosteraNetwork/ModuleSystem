package me.mstn.plugin;

import lombok.Getter;
import me.mstn.plugin.api.SimpleModuleAPI;
import me.mstn.plugin.command.ModuleCommand;
import me.mstn.plugin.manager.ModuleManager;
import me.mstn.plugin.manager.impl.SimpleModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitModuleSystem extends JavaPlugin {

    @Getter
    private static BukkitModuleSystem instance;

    @Getter
    private SimpleModuleAPI moduleAPI;

    @Getter
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;

        moduleAPI = new SimpleModuleAPI(this);
        moduleManager = new SimpleModuleManager(this, moduleAPI);

        getCommand("modules").setExecutor(new ModuleCommand());
    }

    @Override
    public void onDisable() {
        moduleManager.disableModules();
    }

}
