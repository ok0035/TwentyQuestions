package graduateproject.com.twentyquestions.network;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class NetworkSI {

    public String serverAddress;
    public String[][] packet;
    private static NetworkSI networkSI;

    public static NetworkSI getInstance() {
        if(networkSI == null) {
            networkSI = new NetworkSI();
        }

        return networkSI;
    }

    public void request(DataSync.Command data) {

    }

}
