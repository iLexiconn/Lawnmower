package net.ilexiconn.lawnmower.server.integration;

public interface Integration {
    void onInit();

    String getID();

    String getName();
}
