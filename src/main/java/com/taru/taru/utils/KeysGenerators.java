package com.taru.taru.utils;

import java.util.UUID;

/**
 * Created by Shiran Maor on 9/25/2015.
 */
public class KeysGenerators {

    public static String generateID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
