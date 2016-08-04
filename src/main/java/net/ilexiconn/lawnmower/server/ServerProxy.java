package net.ilexiconn.lawnmower.server;

import net.ilexiconn.lawnmower.Lawnmower;
import net.minecraftforge.fml.common.Loader;

public class ServerProxy {
    public void onPreInit() {

    }

    public void onInit() {
        Lawnmower.INTEGRATIONS.stream().filter(integration -> Loader.isModLoaded(integration.getID())).forEach(integration -> {
            integration.onInit();
            Lawnmower.LOGGER.info("Enabled " + integration.getName() + " integration");
        });
    }
}
