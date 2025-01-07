package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.Config;

import java.util.Random;

public class MiscUtil {
    private static final Random random = new Random();
    public static String getMessage() {
        return Config.send_charging_msg ? Config.messages.get(random.nextInt(0, Config.messages.size())) : "";
    }
    public static String getMessagePhoenix() {
        return Config.messagesPhoenix.get(random.nextInt(0, Config.messagesPhoenix.size()));
    }
}
