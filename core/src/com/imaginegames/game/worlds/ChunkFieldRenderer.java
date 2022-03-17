package com.imaginegames.game.worlds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.imaginegames.game.Values;
import com.imaginegames.game.worlds.Chunk;

public class ChunkFieldRenderer {
    private byte fieldWidth, fieldHeight;
    private SpriteBatch sb;
    private Pixmap pix;
    private Texture texture;
    private int textureWidth, textureHeight;
    private ShapeRenderer sr;
    byte chunkSize;

    private boolean renderGrid = false;

    public ChunkFieldRenderer(SpriteBatch sb, byte chunksFieldWidth, byte chunksFieldHeight, byte chunkSize) {
        this.sb = sb;
        this.fieldWidth = chunksFieldWidth;
        this.fieldHeight = chunksFieldHeight;
        this.chunkSize = chunkSize;
        pix = new Pixmap(
                fieldWidth * (int) chunkSize,
                fieldHeight * (int) chunkSize, Pixmap.Format.RGBA8888);
        pix.setBlending(Pixmap.Blending.None);
        textureWidth = chunkSize * fieldWidth;
        textureHeight = chunkSize * fieldHeight;
        sr = new ShapeRenderer();
    }
    public void render(Chunk[][] field, float x, float y) {
        x -= (x >= 0 ? 0 : chunkSize);
        y -= (y >= 0 ? 0 : chunkSize);
        int color = 0xCCCCBBFF, alpha;
        for (int cfx = 0; cfx < fieldWidth; cfx++) {
            for (int cfy = 0; cfy < fieldHeight; cfy++) {
                int cells[][] = field[cfx][cfy].getCells();
                for (int cx = 0; cx < chunkSize; cx++) {
                    for (int cy = 0; cy < chunkSize; cy++) {
                        alpha = cells[cx][cy];
                        pix.drawPixel(cx + chunkSize * cfx, textureHeight - (cy + chunkSize * cfy) - 1, color - alpha);
                    }
                }
            }
        }
        texture = new Texture(pix);
        sb.begin();
        float textureX = -textureWidth / 2 + (int) x / chunkSize * chunkSize + (fieldWidth % 2 != 0 ? chunkSize / 2 : 0);
        float textureY = -textureHeight / 2 + (int) y / chunkSize * chunkSize + (fieldHeight % 2 != 0 ? chunkSize / 2 : 0);

        sb.draw(texture, textureX, textureY, textureWidth, textureHeight);
        sb.end();

        if (Values.debugMode) {
            sr.setProjectionMatrix(sb.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(1, 0, 0, 1);
            sr.rect(textureX, textureY, textureWidth, textureHeight);
            sr.setColor(0, 1, 0, 1);
            for (int i = 1; i < fieldWidth; i++) {
                float xx = textureX + i * chunkSize;
                sr.line(xx, textureY, xx, textureY + textureHeight);
            }
            for (int i = 1; i < fieldHeight; i++) {
                float yy = textureY + i * chunkSize;
                sr.line(textureX, yy, textureX + textureWidth, yy);
            }
            sr.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.YELLOW);
            sr.rect(
                    (int) x / chunkSize * chunkSize,
                    (int) y / chunkSize * chunkSize,
                    chunkSize, chunkSize );
            sr.end();
        }
    }
    public void dispose() {
        pix.dispose();
        texture.dispose();
        sr.dispose();
    }

    public void setRenderGrid(boolean renderGrid) {
        this.renderGrid = renderGrid;
    }

}
