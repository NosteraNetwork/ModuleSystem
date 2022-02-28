package me.mstn.plugin.command;

import me.mstn.plugin.BukkitModuleSystem;
import me.mstn.plugin.manager.ModuleManager;
import me.mstn.plugin.object.IModule;
import me.mstn.plugin.object.ModuleData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModuleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("modulesystem.command")) {
            sender.sendMessage(ChatColor.RED + "[Module System] You don't have permission to use this");

            return false;
        }

        ModuleManager manager = BukkitModuleSystem.getInstance()
                .getModuleManager();

        if (args.length == 0) {
            List<IModule> modules = manager.getModules();
            String message = ChatColor.WHITE + "Modules (" + modules.size() + "): ";

            List<String> moduleList = new ArrayList<>();

            if (modules.size() > 0) {
                for (IModule module : modules) {
                    String name = module.getData().getID();
                    moduleList.add(manager.isActive(module) ? ChatColor.GREEN + name : ChatColor.RED + name);
                }

                message += String.join(", ", moduleList);
            } else
                message += ChatColor.RED + "Empty";

            sender.sendMessage(message);

            return false;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                manager.reloadModules();
                sender.sendMessage(ChatColor.GREEN + "[Module System] Modules reload");
            }

            if (args[0].equalsIgnoreCase("stop")) {
                manager.disableModules();
                sender.sendMessage(ChatColor.GREEN + "[Module System] All modules disabled");
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("disable")) {
                String moduleName = args[1];

                for (IModule module : manager.getActiveModules()) {
                    ModuleData moduleData = module.getData();

                    if (moduleData.getID()
                            .equalsIgnoreCase(moduleName)) {

                        manager.disableModule(module);
                        sender.sendMessage(ChatColor.GREEN + "[Module System] Module \"" + moduleData.getID() +
                                "\" disabled");

                        return false;
                    }
                }

                sender.sendMessage(ChatColor.RED + "[Module System] Module not found or already disabled");
            }

            if (args[0].equalsIgnoreCase("enable")) {
                String moduleName = args[1];

                for (IModule module : manager.getDisabledModules()) {
                    ModuleData moduleData = module.getData();

                    if (moduleData.getID()
                            .equalsIgnoreCase(moduleName)) {

                        manager.enableModule(module);
                        sender.sendMessage(ChatColor.GREEN + "[Module System] Module \"" + moduleData.getID()
                                + "\" enabled");

                        return false;
                    }
                }

                sender.sendMessage(ChatColor.RED + "[Module System] Module not found or already enabled");
            }
        }

        return false;
    }

}
