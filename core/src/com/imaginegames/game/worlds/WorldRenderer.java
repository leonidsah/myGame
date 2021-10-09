package com.imaginegames.game.worlds.limited;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldRenderer {
    private Chunk[][] field;
    private int fieldSize;
    private SpriteBatch sb;
    private Pixmap pix;
    private Texture texture;
    private int textureSize;

    public WorldRenderer(SpriteBatch sb, Chunk[][] chunksField, byte chunksFieldSize) {
        this.sb = sb;
        this.field = chunksField;
        this.fieldSize = chunksFieldSize;
        pix = new Pixmap(
                fieldSize * (int) Chunk.getSize(),
                fieldSize * (int) Chunk.getSize(), Pixmap.Format.RGBA8888);
        pix.setBlending(Pixmap.Blending.None);
        textureSize = Chunk.getSize() * fieldSize;
    }
    public void render(float x, float y) {
        int color = 0x88AADDFF, alpha;
        for (int cfy = 0; cfy < fieldSize; cfy++) {
            for (int cfx = 0; cfx < fieldSize; cfx++) {
                int cells[][] = field[cfy][cfx].getCells();
                for (int cy = 0; cy < Chunk.getSize(); cy++) {
                    for (int cx = 0; cx < Chunk.getSize(); cx++) {
                        alpha = cells[cy][cx];
                        pix.drawPixel(cx + Chunk.getSize() * cfx, cy + Chunk.getSize() * cfy, color - alpha);
                    }
                }
            }
        }
        texture = new Texture(pix);
        sb.begin();
        sb.draw(texture,
                -textureSize / 2 + ((int) x / Chunk.getSize()) * Chunk.getSize(),
                -textureSize / 2 + ((int) y / Chunk.getSize()) * Chunk.getSize(),
                textureSize, textureSize);
        sb.end();
    }
    public void dispose() {
        pix.dispose();
    }
}
