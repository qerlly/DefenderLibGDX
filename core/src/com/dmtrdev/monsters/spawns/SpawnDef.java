package com.dmtrdev.monsters.spawns;

import com.badlogic.gdx.math.Vector2;

public class SpawnDef {

    public Vector2 position;
    public Class<?> type;
    public boolean direction;

    public SpawnDef(final Vector2 pPosition, final Class<?> pType, final boolean pDirection) {
        position = pPosition;
        type = pType;
        direction = pDirection;
    }
}