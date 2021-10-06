package com.imaginegames.game.utils.math;

public final class MapValue {
    public static double map(double val, double oldFloor, double oldCeiling, double newFloor, double newCeiling) {
        return ((val - oldFloor) / (oldCeiling - oldFloor)) * (newCeiling - newFloor) + newFloor;
    }

    public static float map(float val, float oldFloor, float oldCeiling, float newFloor, float newCeiling) {
        return ((val - oldFloor) / (oldCeiling - oldFloor)) * (newCeiling - newFloor) + newFloor;
    }
}
