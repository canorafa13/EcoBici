package com.ecobici.app.classes;

public class EcoBici {
    private Network network;

    public EcoBici(Network network) {
        this.network = network;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
