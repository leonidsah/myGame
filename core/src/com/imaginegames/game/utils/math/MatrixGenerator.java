package com.imaginegames.game.utils.math;
import java.util.Random;

public class MatrixGenerator {
    public static int[][] random() {
        Random random = new Random();
        int[][] cells;
        int width = random.nextInt(500) + 400;
        int height = random.nextInt(312) + 200;
        cells = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = random.nextInt(255);
            }
        }
        return cells;
    }

    public static int[][] random(int width, int height) {
        Random random = new Random();
        int[][] cells;
        cells = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) cells[i][j] = random.nextInt(255);
        }
        return cells;
    }

    public static int[][] linear(int width, int height) {
        int[][] cells;
        cells = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = i + j;
            }
        }
        return cells;
    }

    public static int[][] logarithmic(int width, int height) {
        int[][] cells;
        cells = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = (int) (Math.log(i + j) * 800) % 256;
            }
        }
        return cells;
    }

    public static int[][] perlin(int width, int height) {
        int[][] cells;
        cells = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = (int) (Math.log(i + j) * 100);
            }
        }
        return cells;
    }
}
