package com.imaginegames.game.worlds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.imaginegames.game.Values;

public class ChunkRenderer {
    private final SpriteBatch sb;
    private Texture texture;
    private Pixmap pm;
    private ShapeRenderer sr;
    private int textureWidth, textureHeight;
    private byte fieldWidth, fieldHeight;
    private byte chunkSize;
    private boolean drawGrid = false;

    public ChunkRenderer(SpriteBatch sb, byte chunksFieldWidth, byte chunksFieldHeight, byte chunkSize) {
        this.sb = sb;
        this.fieldWidth = chunksFieldWidth;
        this.fieldHeight = chunksFieldHeight;
        this.chunkSize = chunkSize;
        sr = new ShapeRenderer();
        pm = new Pixmap(fieldWidth * (int) chunkSize,
                fieldHeight * (int) chunkSize, Pixmap.Format.RGBA8888);
        pm.setBlending(Pixmap.Blending.None);
        textureWidth = chunkSize * fieldWidth;
        textureHeight = chunkSize * fieldHeight;
    }

    public void render(Chunk[][] field, float ptrX, float ptrY) {
        int x, y, i, j, alpha, color = 0xCCCCBB00, cells[][];
        float textureX, textureY;
        for (x = 0; x < fieldWidth; x++) {
            for (y = 0; y < fieldHeight; y++) {
                cells = field[x][y].getCells();
                for (i = 0; i < chunkSize; i++) {
                    for (j = 0; j < chunkSize; j++) {
                        alpha = cells[i][j];
                        pm.drawPixel(i + chunkSize * x, textureHeight - (j + chunkSize * y) - 1, color + alpha);
                    }
                }
            }
        }
        ptrX = ptrX - (ptrX >= 0 ? 0 : chunkSize);
        ptrY = ptrY - (ptrY >= 0 ? 0 : chunkSize);
        textureX = -textureWidth / 2 + (int) ptrX / chunkSize * chunkSize + (fieldWidth % 2) * (chunkSize / 2);
        textureY = -textureHeight / 2 + (int) ptrY / chunkSize * chunkSize + (fieldWidth % 2) * (chunkSize / 2);
        texture = new Texture(pm); // TODO: Binding a texture to OpenGL in rendering function leads to noticeable overload
        sb.begin();
        sb.draw(texture, textureX, textureY, textureWidth, textureHeight);
        sb.end();
        if (!Values.debugMode && !drawGrid) return;
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(1, 0, 0, 1);
        sr.rect(textureX, textureY, textureWidth, textureHeight);
        sr.setColor(0, 1, 0, 1);
        for (i = 1; i < fieldWidth; i++) {
            float xx = textureX + i * chunkSize;
            sr.line(xx, textureY, xx, textureY + textureHeight);
        }
        for (i = 1; i < fieldHeight; i++) {
            float yy = textureY + i * chunkSize;
            sr.line(textureX, yy, textureX + textureWidth, yy);
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.YELLOW);
        sr.rect(
                (int) ptrX / chunkSize * chunkSize,
                (int) ptrY / chunkSize * chunkSize,
                chunkSize, chunkSize);
        sr.end();
    }
    public void dispose() {
        pm.dispose();
        texture.dispose();
        sr.dispose();
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

}
