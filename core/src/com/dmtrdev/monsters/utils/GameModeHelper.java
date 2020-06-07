package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.spawns.Generates;
import com.dmtrdev.monsters.sprites.enemies.animals.Earwig;
import com.dmtrdev.monsters.sprites.enemies.animals.Moose;
import com.dmtrdev.monsters.sprites.enemies.bosses.KingOfTheDeath;
import com.dmtrdev.monsters.sprites.enemies.bosses.Storm;
import com.dmtrdev.monsters.sprites.enemies.golems.GolemHigh;
import com.dmtrdev.monsters.sprites.enemies.cyclops.CyclopDefault;
import com.dmtrdev.monsters.sprites.enemies.cyclops.CyclopHigh;
import com.dmtrdev.monsters.sprites.enemies.cyclops.CyclopMedium;
import com.dmtrdev.monsters.sprites.enemies.dragons.DragonDefault;
import com.dmtrdev.monsters.sprites.enemies.dragons.DragonMedium;
import com.dmtrdev.monsters.sprites.enemies.elementals.ElementalDefault;
import com.dmtrdev.monsters.sprites.enemies.elementals.ElementalHigh;
import com.dmtrdev.monsters.sprites.enemies.elementals.ElementalMedium;
import com.dmtrdev.monsters.sprites.enemies.flyings.FlyingDefault;
import com.dmtrdev.monsters.sprites.enemies.flyings.FlyingHigh;
import com.dmtrdev.monsters.sprites.enemies.gargoyle.GargoyleDefault;
import com.dmtrdev.monsters.sprites.enemies.gargoyle.GargoyleHigh;
import com.dmtrdev.monsters.sprites.enemies.gargoyle.GargoyleMedium;
import com.dmtrdev.monsters.sprites.enemies.goblins.GoblinDefault;
import com.dmtrdev.monsters.sprites.enemies.goblins.GoblinHigh;
import com.dmtrdev.monsters.sprites.enemies.goblins.GoblinMedium;
import com.dmtrdev.monsters.sprites.enemies.golems.GolemDefault;
import com.dmtrdev.monsters.sprites.enemies.golems.GolemMedium;
import com.dmtrdev.monsters.sprites.enemies.minotaurs.MinotaurDefault;
import com.dmtrdev.monsters.sprites.enemies.minotaurs.MinotaurHigh;
import com.dmtrdev.monsters.sprites.enemies.minotaurs.MinotaurMedium;
import com.dmtrdev.monsters.sprites.enemies.orcs.OrcDefault;
import com.dmtrdev.monsters.sprites.enemies.orcs.OrcHigh;
import com.dmtrdev.monsters.sprites.enemies.orcs.OrcMedium;
import com.dmtrdev.monsters.sprites.enemies.trolls.TrollDefault;
import com.dmtrdev.monsters.sprites.enemies.trolls.TrollHigh;
import com.dmtrdev.monsters.sprites.enemies.trolls.TrollMedium;
import com.dmtrdev.monsters.sprites.enemies.yetis.YetiDefault;
import com.dmtrdev.monsters.sprites.enemies.yetis.YetiHigh;
import com.dmtrdev.monsters.sprites.enemies.bosses.IcyFoot;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieDefault;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieHigh;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieMedium;

public class GameModeHelper {

    private final String mType;
    private final Array<Class> mEnemyClasses;
    private Array<Class> mBossClasses;
    private final Generates mGenetares;
    private boolean mBossSpawned;

    public GameModeHelper(final Generates pGenerates, final String pType) {
        mType = pType;
        mGenetares = pGenerates;
        mEnemyClasses = new Array<Class>();
        mBossClasses = null;
        mBossSpawned = false;
        setEnemies();
        if (!pType.equals(ConstGame.SURVIVE_MODE)) {
            mEnemyClasses.reverse();
        } else {
            mBossClasses = new Array<Class>();
            mBossClasses.add(IcyFoot.class, Storm.class, KingOfTheDeath.class);
        }
    }

    private void setEnemies() {
        if (isSurviveMode()) {
            mEnemyClasses.addAll(ZombieDefault.class, ZombieMedium.class, ZombieHigh.class, OrcDefault.class, OrcMedium.class, OrcHigh.class, TrollDefault.class, TrollMedium.class, TrollHigh.class, Moose.class,
                    Earwig.class, ElementalDefault.class, ElementalMedium.class, ElementalHigh.class, FlyingDefault.class, FlyingHigh.class,
                    GargoyleDefault.class, GargoyleMedium.class, GargoyleHigh.class, GoblinDefault.class, GoblinMedium.class, GoblinHigh.class, CyclopDefault.class, CyclopMedium.class, CyclopHigh.class, DragonDefault.class, DragonMedium.class, GolemDefault.class,
                    GolemMedium.class, GolemHigh.class, MinotaurDefault.class, MinotaurMedium.class, MinotaurHigh.class, YetiDefault.class, YetiHigh.class);
        } else {
            if (ConstGame.STORY_MODE.equals(mType.substring(1))) {
                switch (mType.charAt(0)) {
                    case '0':
                        mEnemyClasses.addAll(GoblinDefault.class, GoblinDefault.class, ZombieDefault.class, GoblinDefault.class, ZombieMedium.class, ZombieMedium.class, OrcDefault.class, GoblinDefault.class,
                                ZombieMedium.class, OrcDefault.class, ZombieDefault.class, GoblinDefault.class, GoblinDefault.class, ZombieMedium.class, ZombieMedium.class, ZombieMedium.class, GoblinDefault.class,
                                ZombieMedium.class, OrcDefault.class);
                        break;
                    case '1':
                        mEnemyClasses.addAll(GoblinDefault.class, GoblinMedium.class, GoblinMedium.class,  Moose.class, FlyingDefault.class, GoblinMedium.class,  Moose.class, GoblinMedium.class, FlyingDefault.class, TrollDefault.class, TrollDefault.class, GoblinMedium.class, GoblinMedium.class,
                                OrcDefault.class, ZombieMedium.class, OrcDefault.class, Moose.class, TrollDefault.class, TrollDefault.class,  Moose.class, ZombieDefault.class, GoblinMedium.class, GoblinMedium.class, TrollDefault.class, ZombieMedium.class, GoblinMedium.class, Moose.class,
                                FlyingDefault.class, GoblinMedium.class, GoblinMedium.class, TrollDefault.class, Moose.class, OrcDefault.class, ZombieMedium.class, ZombieMedium.class, TrollDefault.class, Moose.class, ZombieMedium.class, GoblinMedium.class,
                                FlyingDefault.class,  Moose.class, FlyingDefault.class, TrollDefault.class, GoblinDefault.class, GoblinDefault.class, Moose.class, Moose.class, ZombieMedium.class, GoblinMedium.class, GoblinMedium.class, ZombieMedium.class, TrollDefault.class, GoblinMedium.class, ZombieMedium.class,
                                GoblinMedium.class); //Убрать челов
                        break;
                    case '2':
                        mEnemyClasses.addAll(OrcMedium.class, GoblinMedium.class, ElementalDefault.class, GoblinMedium.class, ElementalDefault.class, GoblinMedium.class, Moose.class, ElementalDefault.class, TrollDefault.class, ElementalDefault.class,
                                ElementalDefault.class, Moose.class, FlyingDefault.class, FlyingDefault.class, ZombieHigh.class, Moose.class, GoblinMedium.class, GoblinMedium.class, FlyingDefault.class, ElementalDefault.class, ElementalDefault.class, OrcHigh.class, ZombieMedium.class, OrcMedium.class, ElementalDefault.class, Moose.class,
                                OrcMedium.class, TrollDefault.class, Moose.class, GoblinMedium.class, OrcHigh.class, OrcHigh.class, ZombieHigh.class, ElementalDefault.class, ElementalDefault.class,
                                GoblinMedium.class, Moose.class, GoblinMedium.class, FlyingDefault.class, ElementalDefault.class, ElementalDefault.class, Moose.class, OrcHigh.class, TrollDefault.class, Moose.class, ElementalDefault.class, ZombieMedium.class,
                                GoblinMedium.class, FlyingDefault.class, Moose.class, ElementalDefault.class, GoblinMedium.class, GoblinMedium.class, FlyingDefault.class, ElementalDefault.class, ElementalDefault.class, Moose.class, OrcHigh.class, ElementalDefault.class, OrcHigh.class);
                        break;
                    case '3':
                        mEnemyClasses.addAll(GargoyleDefault.class, GoblinHigh.class, ElementalMedium.class, GargoyleDefault.class, GargoyleDefault.class, GoblinHigh.class, Moose.class, OrcHigh.class, ElementalMedium.class, FlyingDefault.class, Moose.class, FlyingDefault.class, GargoyleDefault.class,
                                TrollDefault.class, ElementalDefault.class, GargoyleDefault.class, ElementalMedium.class, GargoyleDefault.class, TrollMedium.class, TrollMedium.class, GargoyleDefault.class, ElementalMedium.class, Moose.class, GargoyleDefault.class, Moose.class,
                                GoblinHigh.class, GargoyleDefault.class, ElementalMedium.class, GoblinHigh.class, GoblinHigh.class, ElementalMedium.class, Moose.class, GoblinMedium.class, FlyingDefault.class, FlyingDefault.class, Moose.class, TrollMedium.class, ZombieHigh.class,
                                ZombieHigh.class, Moose.class, TrollDefault.class, ElementalMedium.class, GargoyleDefault.class, GargoyleDefault.class, Moose.class, Moose.class, GargoyleDefault.class, ElementalMedium.class, GargoyleDefault.class, Moose.class, FlyingDefault.class, OrcHigh.class, Moose.class, GargoyleDefault.class,
                                GoblinHigh.class, GoblinHigh.class, ElementalDefault.class, Moose.class, ZombieHigh.class, TrollMedium.class, GargoyleDefault.class, GoblinHigh.class, ElementalMedium.class, Moose.class, GoblinHigh.class, OrcHigh.class, FlyingDefault.class, Moose.class, FlyingDefault.class,
                                TrollDefault.class);
                        break;
                    case '4':
                        mEnemyClasses.addAll(Earwig.class, GoblinHigh.class, Moose.class, MinotaurDefault.class, GargoyleMedium.class, Moose.class, MinotaurDefault.class, GargoyleMedium.class, GoblinHigh.class, Moose.class, FlyingHigh.class, ZombieHigh.class, FlyingHigh.class, MinotaurDefault.class, OrcHigh.class, TrollMedium.class,
                                Earwig.class, MinotaurDefault.class, GargoyleDefault.class, ZombieHigh.class, GargoyleMedium.class, GoblinHigh.class, GoblinHigh.class, GargoyleMedium.class, TrollMedium.class, ZombieHigh.class, GargoyleMedium.class, ElementalMedium.class,
                                Earwig.class, Moose.class, GargoyleMedium.class, ZombieHigh.class, GargoyleMedium.class, Moose.class, FlyingHigh.class, GargoyleMedium.class, TrollMedium.class, GargoyleMedium.class, GargoyleMedium.class, OrcHigh.class, TrollMedium.class, GoblinHigh.class, GoblinHigh.class,
                                MinotaurDefault.class, MinotaurDefault.class, GargoyleMedium.class, Moose.class, ZombieHigh.class, MinotaurDefault.class, GargoyleMedium.class, MinotaurDefault.class, FlyingHigh.class, FlyingHigh.class, TrollMedium.class, ZombieHigh.class, TrollMedium.class, OrcHigh.class,
                                OrcHigh.class, GargoyleDefault.class, GargoyleMedium.class, ZombieHigh.class, Moose.class, GargoyleMedium.class, Earwig.class, GoblinHigh.class, GargoyleMedium.class, GoblinHigh.class, GargoyleMedium.class, FlyingHigh.class, FlyingHigh.class, MinotaurDefault.class, OrcHigh.class, TrollMedium.class,
                                Earwig.class);
                        break;
                    case '5':
                        mEnemyClasses.addAll(TrollHigh.class, TrollHigh.class, Moose.class, FlyingHigh.class, CyclopDefault.class, ZombieHigh.class, GoblinHigh.class, GargoyleMedium.class, GargoyleMedium.class, ElementalHigh.class, GoblinHigh.class, Moose.class, FlyingHigh.class, ElementalHigh.class,
                                GargoyleMedium.class, GargoyleMedium.class, MinotaurDefault.class, ElementalHigh.class, GoblinHigh.class, Earwig.class, YetiDefault.class, ZombieHigh.class, YetiDefault.class, MinotaurDefault.class,
                                GargoyleMedium.class, FlyingHigh.class, FlyingHigh.class, MinotaurDefault.class, ZombieHigh.class, Earwig.class, GoblinHigh.class, CyclopDefault.class, CyclopDefault.class, GargoyleMedium.class, GargoyleMedium.class, ElementalHigh.class,
                                YetiDefault.class, YetiDefault.class, TrollHigh.class, ElementalHigh.class, GargoyleMedium.class, GargoyleMedium.class, ZombieHigh.class, ElementalHigh.class, TrollHigh.class, YetiDefault.class, Moose.class, FlyingHigh.class, TrollHigh.class, TrollHigh.class, Moose.class,
                                FlyingHigh.class, CyclopDefault.class, GoblinHigh.class, GoblinHigh.class, GargoyleMedium.class, GargoyleMedium.class, ElementalHigh.class, Moose.class, FlyingHigh.class, ElementalHigh.class,
                                GargoyleMedium.class, GargoyleMedium.class, ElementalHigh.class, MinotaurDefault.class, ElementalHigh.class, ZombieHigh.class, GoblinHigh.class, Earwig.class, YetiDefault.class, YetiDefault.class, MinotaurDefault.class,
                                GargoyleMedium.class, FlyingHigh.class, CyclopDefault.class, GoblinHigh.class, GoblinHigh.class, GargoyleMedium.class, GargoyleMedium.class, FlyingHigh.class, MinotaurDefault.class, ZombieHigh.class, Earwig.class, GoblinHigh.class, CyclopDefault.class, CyclopDefault.class,
                                YetiDefault.class, YetiDefault.class, CyclopDefault.class, GoblinHigh.class, GoblinHigh.class, ZombieHigh.class, GargoyleMedium.class, GargoyleMedium.class, TrollHigh.class, ElementalHigh.class, TrollHigh.class, YetiDefault.class, Moose.class, FlyingHigh.class);
                        break;
                    case '6':
                        mEnemyClasses.addAll(GargoyleHigh.class, YetiDefault.class, YetiDefault.class, YetiHigh.class, MinotaurMedium.class, GargoyleHigh.class, FlyingHigh.class, FlyingHigh.class, Moose.class, CyclopDefault.class,
                                GargoyleMedium.class, GargoyleHigh.class, CyclopMedium.class, GargoyleHigh.class, YetiHigh.class, YetiHigh.class, GargoyleHigh.class, TrollHigh.class, ElementalHigh.class, ElementalHigh.class, Moose.class, FlyingHigh.class,
                                CyclopMedium.class, CyclopMedium.class, MinotaurMedium.class, MinotaurMedium.class, GargoyleHigh.class, YetiHigh.class, MinotaurMedium.class, FlyingHigh.class, YetiHigh.class, Moose.class, TrollHigh.class, ElementalHigh.class, FlyingHigh.class, YetiDefault.class,
                                Earwig.class, CyclopMedium.class, YetiHigh.class, MinotaurMedium.class, FlyingHigh.class, CyclopMedium.class, Moose.class, CyclopMedium.class, GargoyleHigh.class, GargoyleHigh.class, YetiHigh.class, GargoyleHigh.class, Earwig.class, FlyingHigh.class, YetiHigh.class, GargoyleHigh.class, MinotaurMedium.class, YetiHigh.class,
                                GargoyleHigh.class, YetiDefault.class, GargoyleHigh.class, YetiDefault.class, YetiHigh.class, MinotaurMedium.class, FlyingHigh.class, FlyingHigh.class, Moose.class, CyclopDefault.class,
                                GargoyleMedium.class, CyclopMedium.class, GargoyleHigh.class, GargoyleHigh.class, YetiHigh.class, YetiHigh.class, TrollHigh.class, ElementalHigh.class, ElementalHigh.class, Moose.class, FlyingHigh.class,
                                CyclopMedium.class, CyclopMedium.class, CyclopMedium.class, GargoyleHigh.class, GargoyleHigh.class, YetiHigh.class, YetiHigh.class, GargoyleHigh.class, MinotaurMedium.class, FlyingHigh.class, GargoyleHigh.class, MinotaurMedium.class, MinotaurMedium.class, YetiHigh.class, Moose.class, TrollHigh.class, ElementalHigh.class, FlyingHigh.class, YetiDefault.class,
                                Earwig.class, CyclopMedium.class, CyclopMedium.class, Moose.class, Earwig.class, FlyingHigh.class, YetiHigh.class, GargoyleHigh.class);
                        break;
                    case '7':
                        mEnemyClasses.addAll(GargoyleHigh.class, MinotaurHigh.class, GargoyleHigh.class, YetiHigh.class, YetiHigh.class, FlyingHigh.class, MinotaurHigh.class, FlyingHigh.class, MinotaurHigh.class, FlyingHigh.class, MinotaurHigh.class, Moose.class, Moose.class, FlyingHigh.class, FlyingHigh.class, MinotaurHigh.class, MinotaurHigh.class, ElementalHigh.class, GargoyleHigh.class, FlyingHigh.class, YetiHigh.class, FlyingHigh.class, Moose.class, ElementalHigh.class, DragonDefault.class, CyclopHigh.class, Moose.class, MinotaurHigh.class,
                                DragonDefault.class, IcyFoot.class, DragonDefault.class, FlyingHigh.class, MinotaurHigh.class, FlyingHigh.class, ElementalHigh.class, FlyingHigh.class, Moose.class, FlyingHigh.class, DragonDefault.class, FlyingHigh.class, MinotaurHigh.class, MinotaurHigh.class, MinotaurHigh.class, Moose.class, MinotaurHigh.class, FlyingHigh.class, ElementalHigh.class, GargoyleHigh.class, YetiHigh.class, FlyingHigh.class, ElementalHigh.class, DragonDefault.class, CyclopHigh.class, Moose.class, MinotaurHigh.class,
                                DragonDefault.class, DragonDefault.class, FlyingHigh.class, MinotaurHigh.class, ElementalHigh.class, FlyingHigh.class, DragonDefault.class, FlyingHigh.class, Moose.class, IcyFoot.class, Moose.class, DragonDefault.class, MinotaurHigh.class, ElementalHigh.class, FlyingHigh.class, FlyingHigh.class, MinotaurHigh.class, ElementalHigh.class, GargoyleHigh.class, YetiHigh.class, YetiHigh.class, FlyingHigh.class, MinotaurHigh.class,
                                FlyingHigh.class, ElementalHigh.class, FlyingHigh.class, MinotaurHigh.class, FlyingHigh.class, ElementalHigh.class, GargoyleHigh.class, FlyingHigh.class, YetiHigh.class, IcyFoot.class, IcyFoot.class);
                        break;
                    case '8':
                        mEnemyClasses.addAll(DragonMedium.class, DragonMedium.class, GolemDefault.class, IcyFoot.class, GolemDefault.class, DragonMedium.class, GolemMedium.class, GolemHigh.class, GolemHigh.class, DragonMedium.class,
                                DragonMedium.class, GolemHigh.class, ElementalHigh.class, IcyFoot.class, GolemDefault.class, DragonMedium.class, GolemMedium.class, ElementalHigh.class, IcyFoot.class, GolemHigh.class, MinotaurHigh.class, MinotaurHigh.class,
                                FlyingHigh.class, Moose.class, DragonMedium.class, DragonMedium.class, GolemDefault.class, GolemDefault.class, IcyFoot.class, MinotaurHigh.class, GolemHigh.class, MinotaurHigh.class, FlyingHigh.class, Moose.class, MinotaurHigh.class,
                                GolemMedium.class, MinotaurHigh.class, IcyFoot.class, IcyFoot.class, FlyingHigh.class, Moose.class, MinotaurHigh.class, DragonMedium.class, GolemMedium.class, ElementalHigh.class, GolemHigh.class, MinotaurHigh.class, DragonMedium.class, GolemMedium.class, IcyFoot.class);
                        break;
                    case '9':
                        mEnemyClasses.addAll(FlyingHigh.class, IcyFoot.class, DragonMedium.class, FlyingHigh.class, IcyFoot.class, DragonMedium.class, FlyingHigh.class, GolemHigh.class, Moose.class, GolemHigh.class, MinotaurHigh.class, MinotaurHigh.class, GolemHigh.class, Moose.class, MinotaurHigh.class, GolemHigh.class,
                                YetiHigh.class, IcyFoot.class, YetiHigh.class, DragonMedium.class, MinotaurHigh.class, Storm.class, KingOfTheDeath.class, GolemHigh.class, FlyingHigh.class, FlyingHigh.class, MinotaurHigh.class, YetiHigh.class, DragonMedium.class, MinotaurHigh.class, GolemHigh.class, FlyingHigh.class, FlyingHigh.class, MinotaurHigh.class,
                                YetiHigh.class, Storm.class, Storm.class);
                        break;
                }
            } else if (ConstGame.CHALLENGE_MODE.equals(mType.substring(1))) {
                switch (mType.charAt(0)) {
                    case '0':
                        mEnemyClasses.addAll(Moose.class, FlyingDefault.class, FlyingHigh.class, FlyingHigh.class, FlyingDefault.class, FlyingHigh.class, Moose.class, FlyingDefault.class, FlyingHigh.class, Moose.class, Moose.class, Earwig.class,
                                Moose.class, Moose.class, FlyingHigh.class, Earwig.class, FlyingHigh.class, Moose.class, FlyingDefault.class, FlyingDefault.class, Moose.class, Moose.class, FlyingHigh.class, Moose.class,
                                Moose.class, FlyingHigh.class, FlyingHigh.class, Moose.class, FlyingHigh.class, FlyingHigh.class, Moose.class, FlyingDefault.class, FlyingHigh.class, Earwig.class, FlyingHigh.class, FlyingDefault.class, FlyingHigh.class, Moose.class, FlyingDefault.class, FlyingHigh.class, Moose.class, Moose.class, Earwig.class,
                                Moose.class, Moose.class, Earwig.class, Moose.class, FlyingHigh.class, FlyingHigh.class, FlyingHigh.class, FlyingHigh.class, Earwig.class, Moose.class, FlyingHigh.class, FlyingHigh.class, Moose.class, Moose.class, FlyingDefault.class, FlyingHigh.class, FlyingHigh.class, FlyingDefault.class, FlyingHigh.class, Moose.class, FlyingDefault.class, FlyingHigh.class, Moose.class, Moose.class, Earwig.class,
                                FlyingHigh.class, Earwig.class, Moose.class, Moose.class, FlyingHigh.class, Earwig.class, Earwig.class);
                        break;
                    case '1':
                        mEnemyClasses.addAll(GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinMedium.class, GoblinMedium.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class,
                                GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinMedium.class, GoblinMedium.class, GoblinHigh.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinMedium.class,
                                GoblinMedium.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinMedium.class, GoblinMedium.class, GoblinHigh.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinMedium.class,
                                GoblinMedium.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinMedium.class, GoblinMedium.class, GoblinHigh.class, GoblinHigh.class, GoblinHigh.class, GoblinMedium.class, GoblinMedium.class, GoblinHigh.class, GoblinDefault.class, GoblinDefault.class, GoblinDefault.class, GoblinMedium.class);
                        break;
                    case '2':
                        mEnemyClasses.addAll(Storm.class, FlyingHigh.class, FlyingHigh.class, Storm.class, FlyingHigh.class, FlyingDefault.class, Storm.class, FlyingHigh.class, FlyingHigh.class, FlyingHigh.class, Storm.class, FlyingHigh.class, Storm.class, FlyingHigh.class, FlyingHigh.class);
                        break;
                    case '3':
                        mEnemyClasses.addAll(CyclopDefault.class, CyclopHigh.class, GolemDefault.class, TrollMedium.class, GolemMedium.class, CyclopMedium.class, GolemHigh.class, TrollDefault.class, TrollHigh.class, CyclopMedium.class, GolemHigh.class, TrollDefault.class, TrollHigh.class,
                                CyclopDefault.class, CyclopHigh.class, GolemDefault.class, TrollMedium.class, GolemMedium.class, CyclopMedium.class, GolemHigh.class, CyclopHigh.class, GolemDefault.class, TrollMedium.class, GolemMedium.class, CyclopMedium.class, GolemHigh.class, TrollDefault.class, TrollHigh.class);
                        break;
                    case '4':
                        mEnemyClasses.addAll(KingOfTheDeath.class);
                        break;
                }
            }
        }
    }

    public Class getEnemyForWave(final int pIndex, final int pWaveNumber) {
        if (isSurviveMode()) {
            if (pWaveNumber % 5 == 0 && !mBossSpawned) {
                mBossSpawned = true;
                return mBossClasses.get(MathUtils.random(mBossClasses.size - 1));
            }

            if (mGenetares.getWaveIndex() < 5) {
                return mEnemyClasses.get(MathUtils.random(22 - 1));
            } else {
                return mEnemyClasses.get(MathUtils.random(mEnemyClasses.size - 1));
            }
        } else {
            return mEnemyClasses.get(pIndex);
        }
    }

    public void setBossSpawn(final boolean pSpawned) {
        mBossSpawned = pSpawned;
    }

    public int getEnemiesCount() {
        return mEnemyClasses.size - 1;
    }

    public int getEnemiesCount(final int pIndex) {
        return ConstEnemies.SPAWN_DEFAULT_COUNT + pIndex;
    }

    public int getLevel() {
        return Integer.parseInt(String.valueOf(mType.charAt(0))) + 1;
    }

    public boolean isSurviveMode() {
        return mType.equals(ConstGame.SURVIVE_MODE);
    }

    public boolean isStoryMode() {
        return mType.substring(1).equals(ConstGame.STORY_MODE);
    }

    public boolean isChallengeMode() {
        return mType.substring(1).equals(ConstGame.CHALLENGE_MODE);
    }
}