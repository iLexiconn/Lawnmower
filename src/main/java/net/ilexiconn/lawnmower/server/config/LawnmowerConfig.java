package net.ilexiconn.lawnmower.server.config;

import net.ilexiconn.llibrary.server.config.ConfigEntry;

public class LawnmowerConfig {
    @ConfigEntry(comment = "Enable flying lawnmower, also rustles your jimmies")
    public boolean flyingLawnmower = false;

    @ConfigEntry(comment = "Lawn regrow tick rate")
    public int lawnTickRate = 10;

    @ConfigEntry(comment = "Grow weeds on grass")
    public boolean growWeeds = true;
}
