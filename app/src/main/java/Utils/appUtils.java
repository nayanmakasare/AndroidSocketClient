package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class appUtils
{

    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }


    public static String getWifiMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/wlan0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
