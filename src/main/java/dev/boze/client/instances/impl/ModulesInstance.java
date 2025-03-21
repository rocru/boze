package dev.boze.client.instances.impl;

import dev.boze.api.exception.ModuleNotFoundException;
import dev.boze.api.internal.interfaces.IModules;
import dev.boze.client.Boze;
import dev.boze.client.systems.modules.Module;

public class ModulesInstance implements IModules {
    public boolean getState(String module) throws ModuleNotFoundException {
        Module var5 = Boze.getModules().method395(module);
        if (var5 == null) {
            throw new ModuleNotFoundException(module + " not found");
        } else {
            return var5.isEnabled();
        }
    }

    public void setState(String module, boolean state) throws ModuleNotFoundException {
        Module var6 = Boze.getModules().method395(module);
        if (var6 == null) {
            throw new ModuleNotFoundException(module + " not found");
        } else {
            var6.setEnabled(state);
        }
    }
}
