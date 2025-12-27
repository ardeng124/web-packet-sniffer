package backendsrc.service;

import java.util.List;

public class NetworkInterfaceInfo {
    public String name;
    public String description;
    public List<String> addresses;
    public String macAddress;

    public NetworkInterfaceInfo(String nameIn, String descIn, List<String> addressesIn, String macIn ){
        name = nameIn;
        description = descIn;
        addresses = addressesIn;
        macAddress = macIn;
    }

    public NetworkInterfaceInfo(String nameIn, String descIn, List<String> addressesIn){
        name = nameIn;
        description = descIn;
        addresses = addressesIn;
    }

    public String toString(){
        return name+"   desc: "+description+"   address: "+addresses.toString();
    }
}
