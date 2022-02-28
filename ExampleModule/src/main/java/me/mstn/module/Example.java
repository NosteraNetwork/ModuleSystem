package me.mstn.module;

import me.mstn.plugin.object.IModule;
import me.mstn.plugin.object.ModuleData;

public class Example extends IModule {

    public void onStart() {
        this.getModuleSystem()
                .getLogger()
                .info("[ExampleModule] Module started!");
    }

    public void onStop() {

    }

    public ModuleData getData() {
        return new ModuleData() {
            public String getID() {
                return "example";
            }

            public String getAuthor() {
                return "Masston";
            }

            public String getVersion() {
                return "1.0b";
            }
        };
    }

}
