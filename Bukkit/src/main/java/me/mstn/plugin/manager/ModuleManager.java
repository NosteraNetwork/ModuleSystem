package me.mstn.plugin.manager;

import me.mstn.plugin.object.IModule;

import java.util.Set;

public interface ModuleManager {

    Set<IModule> getModules();

    Set<IModule> getActiveModules();

    Set<IModule> getDisabledModules();

    void disableModules();

    void reloadModules();

    void disableModule(IModule module);

    void enableModule(IModule module);

    boolean isActive(IModule module);

    default boolean isModule(Class<?> object) {
        try {
            object.asSubclass(IModule.class);
        } catch (ClassCastException exception) {
            return false;
        }

        return true;
    }

}
