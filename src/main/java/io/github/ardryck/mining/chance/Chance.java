package io.github.ardryck.mining.chance;

import lombok.Getter;

@Getter
public enum Chance {

    IRON(4, 3.6, 7.2),
    GOLD(2.6, 3.0, 8.0),
    COAL(6.0, 5.0, 10.0),
    LAPIS(0, 4.9, 9.8),
    REDSTONE(0, 4.0, 8.0),
    DIAMOND(0, 2.0, 8.0),
    EMERALD(0, 1.5, 3.0);

    private final double above;
    private final double bellow;
    private final double extreme_hills;
    
    Chance(double above, double bellow, double extreme_hills) {
        this.above = above;
        this.bellow = bellow;
        this.extreme_hills = extreme_hills;
    }
}
