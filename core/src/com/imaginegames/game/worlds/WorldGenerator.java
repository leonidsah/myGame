package com.imaginegames.game.worlds;

import java.util.Random;

public class WorldGenerator {

    public WorldGenerator() { }

    public static World generateRandom() {
        int worldWidth, worldHeight;
        int[][] blocks;
        Random random = new Random();
        worldWidth = random.nextInt(500) + 400;
        worldHeight = random.nextInt(312) + 200;
        blocks = new int[worldHeight][worldWidth];
        for (int i = 0; i < worldHeight; i++) {
            for (int j = 0; j < worldWidth; j++) {
                blocks[i][j] = random.nextInt(255);
            }
        }
        return new World(blocks);
    }
}
