package dev.boze.client.instances.impl;

import dev.boze.api.client.cape.CapeSource;
import dev.boze.api.internal.interfaces.ICapes;

import java.util.ArrayList;

public class CapesInstance implements ICapes {
    public final ArrayList<CapeSource> field2092 = new ArrayList();

    public void addSource(CapeSource source) {
        this.field2092.add(source);
    }

    public void removeSource(CapeSource source) {
        this.field2092.remove(source);
    }

    public String[] getSources() {
        String[] var4 = new String[this.field2092.size() + 1];
        var4[0] = "Boze Capes Server";
        int var5 = 1;

        for (CapeSource var7 : this.field2092) {
            var4[var5] = var7.name;
            var5++;
        }

        return var4;
    }
}
