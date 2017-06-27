package com.blackberry.monkeysimulatior.util;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by ruins7 on 2017-06-25.
 */

public class AssembleMonkeyCommand {

    public static String assembleMonkeyCommand(@NonNull MonkeySettings monkeySettingsObj){
        // TODO assmble Monkey command
        // command example: adb shell monkey 500 -v
        String finalAdbCommandString = "monkey ";
        for (String value : monkeySettingsObj.getAllMonkeySettingsValues(monkeySettingsObj)) {
            finalAdbCommandString += value+" ";
        }
        return finalAdbCommandString.trim();
    }

}
