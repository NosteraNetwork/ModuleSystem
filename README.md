# Bukkit Module System

## General information
The project has no purpose (only bad code, hehe).
I liked the whole idea of modules for a regular Bukkit server, with the ability to manipulate them.

At the moment the project has:
- Open source
- Empty api as a form to fill it out
- Module manager
- Command for easy management of modules outside the code

## Command
At the moment, there is only 1 command in the plugin and several aliases for it.
- /module
    - /module - Listing active modules. If there are no active modules, the command will return the response - Empty.
    - /module reload - Reloads all modules (disabled modules will be re-enabled)
    - /module stop - Stop all modules
    - /module disable/enable <Module ID> - Disable/Enable module (if exists)

Aliases: /modules; /модули; /модуль (Permission: modulesystem.command)

## Configuration

(Example)

![Configuration example](https://i.imgur.com/VWVPQ69.png)

- auto-load - If the parameter is enabled, at startup, all modules in the configuration will be automatically enabled
- modules - List of modules