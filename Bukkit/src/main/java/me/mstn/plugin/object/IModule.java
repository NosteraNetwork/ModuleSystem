package me.mstn.plugin.object;

import me.mstn.plugin.BukkitModuleSystem;
import me.mstn.plugin.api.SimpleModuleAPI;

import java.io.File;

public abstract class IModule {

    public abstract void onStart();

    public abstract void onStop();

    public abstract ModuleData getData();

    public BukkitModuleSystem getModuleSystem() {
        return BukkitModuleSystem.getInstance();
    }

    public SimpleModuleAPI getAPI() {
        return BukkitModuleSystem.getInstance()
                .getModuleAPI();
    }

    public File getDirectory() {
        try {
            return new File(getAPI()
                    .getPathToModules(), getData().getID());
        } catch (Exception exception) {
            return null;
        }
    }

}
