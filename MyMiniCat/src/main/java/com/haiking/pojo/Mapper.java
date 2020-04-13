package com.haiking.pojo;

public class Mapper {
    private Host host;

    public Host getHost() {
        if (host==null)
            host = new Host();
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
