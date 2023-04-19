package com.imaginegames.game.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.imaginegames.game.Values;

import java.io.Serializable;

public class ProceduralWorld implements Serializable {
    static final long serialVersionUID = 2L;
    private final String name;
    private final long seed;
    private final byte chunkSize;

    /**
     * worldName - needs to distinguish worlds with the same seed, but different changes
     * worldSeed - needs to know how to procedurally expand the world, when player explores it
     * worldDirectory - needs to know where to store data about the world, (like chunks state,
     *      player position, and etc.)
     * */
    public ProceduralWorld(String worldName, long worldSeed, byte chunkSize) {
        this.name = worldName;
        this.seed = worldSeed;
        this.chunkSize = chunkSize;
    }

    public long getSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    public byte getChunkSize() { return chunkSize; }

    public FileHandle getDirectory() {
        return Gdx.files.external(Values.WORLDS_DIR_PATH + "/" + name);
    }
}
