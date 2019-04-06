package model;

import android.net.nsd.NsdServiceInfo;

public class NsdServiceFound {

    public NsdServiceInfo getNsdServiceInfo() {
        return nsdServiceInfo;
    }

    public void setNsdServiceInfo(NsdServiceInfo nsdServiceInfo) {
        this.nsdServiceInfo = nsdServiceInfo;
    }

    private NsdServiceInfo nsdServiceInfo ;

    public NsdServiceFound(NsdServiceInfo nsdServiceInfo)
    {
        this.nsdServiceInfo = nsdServiceInfo;
    }
}
