package com.imaginegames.game.debug;

public class Timer {
    private static long begin;

    public static void begin() {
        begin = System.nanoTime();
    }

    public static void end() {
        long time = System.nanoTime() - begin;
        System.out.printf("\r\bTime: %d (%f millis)", time, time / 10000000f);
    }

    public static long end(String outMessage) {
        long time = System.nanoTime() - begin;
        System.out.printf("\r\b%s: %d (%f millis)", outMessage, time, time / 10000000f);
        return time;
    }
}
