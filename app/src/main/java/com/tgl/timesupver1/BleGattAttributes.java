package com.tgl.timesupver1;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
class BleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    static String TEST = "00000002-710e-4a5b-8d75-3e5b444bc3cf";

    static {
        attributes.put(TEST, "Exam Code");
    }

    static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
