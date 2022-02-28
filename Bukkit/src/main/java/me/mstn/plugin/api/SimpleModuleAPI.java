package me.mstn.plugin.api;

import lombok.Getter;
import me.mstn.plugin.BukkitModuleSystem;

import java.io.File;

public class SimpleModuleAPI {

    @Getter
    private final File pathToModules;

    public SimpleModuleAPI(BukkitModuleSystem plugin) {
        this.pathToModules = new File(plugin.getDataFolder(), "modules/");
    }

    // Any methods can be added to this class, it can be used from a class that inherits IModule

}
