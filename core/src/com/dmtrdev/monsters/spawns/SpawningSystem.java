package com.dmtrdev.monsters.spawns;

public interface SpawningSystem {
  void handleSpawning();
  void spawn(final SpawnDef pSpawnDef);
}