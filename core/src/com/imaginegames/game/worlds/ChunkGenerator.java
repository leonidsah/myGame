package com.imaginegames.game.worlds;

import com.imaginegames.game.utils.math.FastNoiseLite;
import com.imaginegames.game.utils.math.PerlinNoise;

public class ChunkGenerator {

    public ChunkGenerator() {}

    public static Chunk generateChunk(int x, int y, long seed, byte chunkSize) {
        return generateChunk3(x, y, seed, chunkSize);
    }

    public static Chunk generateChunk1(int x, int y, long seed, byte chunkSize) {
        FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        float z = (float) seed;
        int[][] cells = new int[chunkSize][chunkSize];
        for (int cy = 0; cy < chunkSize; cy++)
            for (int cx = 0; cx < chunkSize; cx++) {
                double perlinValue = noise.GetNoise((cx) * 5.0f, (cy) * 5.0f, z * 25.0f);
                cells[cy][cx] = ((int) ((Math.pow(perlinValue, 0.75) + 1.0f) / 2.0f * 255)) % 256;
            }
        return new Chunk(cells, chunkSize);
    }

    public static Chunk generateChunk2(int x, int y, long seed, byte chunkSize) {
        int i = (x * y) % 256;
        float z = (float) seed;
        int[][] cells = new int[chunkSize][chunkSize];
        for (int cy = 0; cy < chunkSize; cy++)
            for (int cx = 0; cx < chunkSize; cx++) {
                cells[cy][cx] = i;
                i = (i + 1) % 256;
            }
        return new Chunk(cells, chunkSize);
    }

    public static Chunk generateChunk3(int x, int y, long seed, byte chunkSize) {
        int[][] cells = new int[chunkSize][chunkSize];
        if (y >= 0) {
            for (int cy = 0; cy < chunkSize; cy++)
                for (int cx = 0; cx < chunkSize; cx++) {
                    cells[cy][cx] = 0;
                }
            return new Chunk(cells, chunkSize);
        }
        int i = (x * y) % 256;
        float z = (float) seed;
        for (int cy = 0; cy < chunkSize; cy++)
            for (int cx = 0; cx < chunkSize; cx++) {
                cells[cy][cx] = i;
                i = (i + 1) % 256;
            }
        return new Chunk(cells, chunkSize);
    }
}
