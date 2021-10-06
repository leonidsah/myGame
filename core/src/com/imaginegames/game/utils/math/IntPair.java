package com.imaginegames.game.utils.math;

import java.util.Objects;

public class IntPair {
    int x, y;
    public IntPair(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof IntPair) {
            if (this.getX() == ((IntPair)obj).getX() && this.getY() == ((IntPair)obj).getY()) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
