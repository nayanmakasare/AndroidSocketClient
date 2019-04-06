package model;

import android.net.nsd.NsdServiceInfo;

public class NsdServiceResolve {

    public NsdServiceInfo getNsdServiceInfo() {
        return nsdServiceInfo;
    }

    public void setNsdServiceInfo(NsdServiceInfo nsdServiceInfo) {
        this.nsdServiceInfo = nsdServiceInfo;
    }

    private NsdServiceInfo nsdServiceInfo;

    public boolean isLinkedDevice() {
        return isLinkedDevice;
    }

    public void setLinkedDevice(boolean linkedDevice) {
        isLinkedDevice = linkedDevice;
    }

    private boolean isLinkedDevice;


    public NsdServiceResolve(NsdServiceInfo nsdServiceInfo)
    {
        this.nsdServiceInfo = nsdServiceInfo;
    }
}
