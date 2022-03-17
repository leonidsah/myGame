package com.imaginegames.game.worlds;

import com.imaginegames.game.utils.math.FastNoiseLite;
import com.imaginegames.game.utils.math.PerlinNoise;

public class ChunkGenerator {

    public ChunkGenerator() {}

    public static Chunk generateChunk(long seed, byte chunkSize) {
        FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        float z = (float) seed;
        int[][] cells = new int[chunkSize][chunkSize];
        for (int cy = 0; cy < chunkSize; cy++)
            for (int cx = 0; cx < chunkSize; cx++) {
                double perlinValue = noise.GetNoise((cx) * 5.0f, (cy) * 5.0f, z * 25.0f);
                //cells[cy][cx] = (int) ((Math.pow(perlinValue, 0.75) + 1.0f) / 2.0f * 255);
                cells[cy][cx] = 5;
            }
        return new Chunk(cells, chunkSize);
    }
}
