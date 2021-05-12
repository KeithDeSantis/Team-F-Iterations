package edu.wpi.cs3733.D21.teamF.utils;

import java.util.HashMap;
import java.util.List;

/**
 * Used to read keys from our .env file
 * @author ahf dpm
 */
public class KeyManager {

    private final HashMap<String, String> keys;



    private KeyManager() {
        keys = new HashMap<>();

        try {
            List<String[]> data = CSVManager.load(".env");
            if(data.isEmpty())
                return;
            if(data.get(0).length != 2)
                return;

            //intellij suggested 'datum' here. I'm keeping it.
            for (String[] datum : data) {
                keys.put(datum[0], datum[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getKey(String key) { return keys.get(key); }

    private static class KeyManagerSingletonHelper {
        private static final KeyManager keyManager = new KeyManager();
    }

    public static KeyManager getKeyManager() {
        return KeyManagerSingletonHelper.keyManager;
    }
}
