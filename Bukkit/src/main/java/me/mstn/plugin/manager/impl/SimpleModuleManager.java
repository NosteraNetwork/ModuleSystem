package me.mstn.plugin.manager.impl;

import lombok.Getter;
import me.mstn.plugin.BukkitModuleSystem;
import me.mstn.plugin.api.SimpleModuleAPI;
import me.mstn.plugin.manager.ModuleManager;
import me.mstn.plugin.object.IModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/*
* The code of this project looks very bad. I am ashamed.
* I'm sure it can be done better, but I'm too lazy.
* */

public class SimpleModuleManager implements ModuleManager {

    private final BukkitModuleSystem plugin;

    @Getter
    private final List<IModule> modules = new ArrayList<>();
    private final SimpleModuleAPI moduleAPI;

    @Getter
    private final List<IModule> disabledModules = new ArrayList<>();

    public SimpleModuleManager(BukkitModuleSystem plugin, SimpleModuleAPI moduleAPI) {
        this.plugin = plugin;
        this.moduleAPI = moduleAPI;

        plugin.getLogger()
                .info("ModuleManager: Starting module loader");

        loadModules();

        plugin.getLogger()
                .info("ModuleManager: " + modules.size() + " modules loaded!");
    }

    private boolean isRegistered(String name) {
        for (IModule module : modules) {
            if (module.getData().getID().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    private void loadModules() {
        FileConfiguration configuration = plugin.getConfig();
        boolean start = configuration.getBoolean("auto-load");

        File modulesPath = moduleAPI.getPathToModules();

        if (!modulesPath.exists())
            modulesPath.mkdir();

        File[] moduleFiles = modulesPath.listFiles();

        if (moduleFiles == null || moduleFiles.length == 0)
            return;

        for (File moduleFile : moduleFiles) {
            try {
                if (!moduleFile.toString().endsWith(".jar"))
                    continue;

                JarFile jarFile = new JarFile(moduleFile);
                Enumeration<JarEntry> entries = jarFile.entries();

                URLClassLoader loader = new URLClassLoader(
                        new URL[]{ moduleFile.toURI().toURL() },
                        plugin.getClass()
                                .getClassLoader()
                );

                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();

                    if (jarEntry.isDirectory())
                        continue;

                    if (!jarEntry.getName().endsWith(".class"))
                        continue;

                    String entryName = jarEntry.getName();
                    String className = entryName.substring(0, entryName.length() - 6)
                            .replace('/', '.');

                    Class<?> classObject = loader.loadClass(className);

                    if (!isModule(classObject))
                        continue;

                    try {
                        IModule module = (IModule) classObject.newInstance();
                        String id = module.getData().getID();

                        if (isRegistered(id)) {
                            plugin.getLogger().severe("ModuleManager: Module with name \"" + id
                                    + "\" already registered!");
                            return;
                        }

                        boolean enabled = configuration
                                .getBoolean("modules." + id + ".enabled");

                        if (!configuration.contains("modules." + id)) {
                            configuration.set("modules." + id + ".enabled", true);
                            enabled = true;
                        }

                        if (start && enabled)
                            module.onStart();
                        else
                            disabledModules.add(module);

                        modules.add(module);

                        plugin.getLogger().info("ModuleManager: Registered new module: " + id);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        plugin.saveConfig();
    }

    @Override
    public void disableModule(IModule module) {
        disabledModules.add(module);
        module.onStop();
    }

    @Override
    public void enableModule(IModule module) {
        if (!disabledModules.contains(module))
            return;

        disabledModules.remove(module);
        module.onStart();
    }

    @Override
    public List<IModule> getActiveModules() {
        return modules
                .stream()
                .filter(module -> !disabledModules.contains(module))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isActive(IModule module) {
        return !disabledModules.contains(module);
    }

    @Override
    public void disableModules() {
        modules.forEach(IModule::onStop);
    }

    @Override
    public void reloadModules() {
        disabledModules.forEach(disabledModules::remove);
        modules.forEach(IModule::onStart);
    }

}
