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
     * worldName - how to call this world. In essence, needs just to differentiate worlds with the
     *      same seed, but different changes
     * worldSeed - necessary to know how to generate world, while player explores it
     * worldDirectory - necessary to know where to save newly generated chunks and modified old ones;
     *      also needed to know, where player spawns at beginning of another game and where to
     *      draw chunks
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
