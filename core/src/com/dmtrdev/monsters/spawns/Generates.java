package com.dmtrdev.monsters.spawns;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.enemies.Enemy;
import com.dmtrdev.monsters.sprites.enemies.animals.Earwig;
import com.dmtrdev.monsters.sprites.enemies.animals.Moose;
import com.dmtrdev.monsters.sprites.enemies.bosses.IcyFoot;
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
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.sprites.enemies.yetis.YetiDefault;
import com.dmtrdev.monsters.sprites.enemies.yetis.YetiHigh;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieDefault;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieHigh;
import com.dmtrdev.monsters.sprites.enemies.zombies.ZombieMedium;
import com.dmtrdev.monsters.sprites.world.Coin;
import com.dmtrdev.monsters.utils.GameModeHelper;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Generates implements com.dmtrdev.monsters.spawns.SpawningSystem {

    private final PlayScreen mPlayScreen;
    private final Array<Enemy> mEnemies;
    private final Array<Coin> mCoins;
    private final BlockingDeque<com.dmtrdev.monsters.spawns.SpawnDef> mEnemiesToSpawn;
    private final BitmapFont mFont;
    private final GlyphLayout mLayout;
    private final GameModeHelper mGameModeHelper;
    private final Random mRandom;
    private int mSpawnCount, mWaveIndex, mScore, mDeadEnemies;
    private float mEnemyDelay;
    private final short mWaveDelay;
    private long mWaveStartTimerDiff, mWaveStartTimer;
    private boolean mWaveStart;
    private byte mTutorialFlag;
    private float mTutorialTimer;

    public Generates(final PlayScreen pPlayScreen) {
        mPlayScreen = pPlayScreen;
        mEnemies = new Array<>();
        mCoins = new Array<>();
        mRandom = new Random();
        mEnemiesToSpawn = new LinkedBlockingDeque<>();
        mGameModeHelper = new GameModeHelper(this, pPlayScreen.getType());
        mWaveIndex = mSpawnCount = mScore = mDeadEnemies = 0;
        mEnemyDelay = 0;
        mWaveDelay = 2000;
        mWaveStartTimer = mWaveStartTimerDiff = 0;
        mWaveStart = false;

        mFont = DefenderOfNature.getFont();
        mLayout = new GlyphLayout();
        mFont.setUseIntegerPositions(false);
        mFont.getData().setScale(0.033f);
        mTutorialFlag = 0;
        mTutorialTimer = 0;
    }

    public void updateGenerations(final float delta) {
        handleSpawning();
        if (!mPlayScreen.getOptions().firstStart() || !mPlayScreen.getType().contains(ConstGame.STORY_MODE)) {
            if (!mWaveStart && mWaveStartTimer == 0) {
                if (mGameModeHelper.isSurviveMode()) {
                    mSpawnCount = mGameModeHelper.getEnemiesCount(mWaveIndex);
                    mPlayScreen.getTree().restoreHp(mWaveIndex);
                    mGameModeHelper.setBossSpawn(false);
                    mScore += mWaveIndex;
                    mWaveIndex++;
                } else {
                    mSpawnCount = mGameModeHelper.getEnemiesCount();
                }
                mWaveStartTimer = System.nanoTime();
                mEnemyDelay = 5;
            } else {
                mWaveStartTimerDiff = (System.nanoTime() - mWaveStartTimer) / 1000000;
                if (mWaveStartTimerDiff > mWaveDelay) {
                    mWaveStart = true;
                    mWaveStartTimer = 0;
                }
            }
            mEnemyDelay += delta;

            if (mWaveStart) {
                if (mEnemies.size == 0 && mSpawnCount == -1 && mCoins.size == 0) {
                    if (mGameModeHelper.isSurviveMode()) {
                        mWaveStart = false;
                        mEnemies.clear();
                    } else if (mGameModeHelper.isStoryMode()) {
                        mPlayScreen.setLevelStory(true, mGameModeHelper.getLevel());
                    } else if (mGameModeHelper.isChallengeMode()) {
                        mPlayScreen.setLevelChallenge(true, mGameModeHelper.getLevel());
                    }
                } else if (mSpawnCount != -1 && mEnemyDelay > getEnemySpawnDelay() && getSpawnPlace()) {
                    mEnemyDelay = 0;
                    spawn(new SpawnDef(new Vector2(ConstEnemies.SPAWN_LEFT, ConstEnemies.SPAWN_HEIGHT), mGameModeHelper.getEnemyForWave(mSpawnCount, mWaveIndex), true));
                    mSpawnCount--;
                } else if (mSpawnCount != -1 && mEnemyDelay > getEnemySpawnDelay() && !getSpawnPlace()) {
                    mEnemyDelay = 0;
                    spawn(new SpawnDef(new Vector2(ConstEnemies.SPAWN_RIGHT, ConstEnemies.SPAWN_HEIGHT), mGameModeHelper.getEnemyForWave(mSpawnCount, mWaveIndex), false));
                    mSpawnCount--;
                }
            }
        } else {
            mTutorialTimer += delta;
            initTutorial();
        }
        for (int i = 0; i < mEnemies.size; i++) {
            if (mEnemies.get(i).getDestroyed()) {
                if(mPlayScreen.getOptions().firstStart()){
                    mTutorialTimer = 0;
                }
                mScore += mEnemies.get(i).getScore();
                mDeadEnemies++;
                mEnemies.removeIndex(i);
                i--;
            } else if (mEnemies.get(i).getCoinSpawn() && !mPlayScreen.getOptions().firstStart()) {
                if (mRandom.nextInt(6) >= 4) {
                    mCoins.add(new Coin(mPlayScreen, mEnemies.get(i).getScore(), mEnemies.get(i).getBody().getPosition().x, mEnemies.get(i).getBody().getPosition().y));
                    mEnemies.get(i).setCoinSpawn(false);
                } else {
                    mEnemies.get(i).setCoinSpawn(false);
                }
            } else {
                mEnemies.get(i).update(delta);
            }
        }
        for (int i = 0; i < mCoins.size; i++) {
            if (mCoins.get(i).isDestroyed()) {
                mCoins.removeIndex(i);
                i--;
            } else {
                mCoins.get(i).update(delta);
            }
        }
    }

    public int getScore() {
        return mScore;
    }

    public int getDeadEnemies() {
        return mDeadEnemies;
    }

    public int getWaves() {
        return mWaveIndex - 1;
    }

    public void drawEnemies(final Batch pBatch) {
        for (final Enemy enemy : mEnemies) {
            enemy.draw(pBatch);
        }
        for (final Coin coin : mCoins) {
            coin.draw(pBatch);
        }
        if (mPlayScreen.getType().contains(ConstGame.STORY_MODE) && mPlayScreen.getOptions().firstStart()) {
            if (mTutorialFlag == 0 && mTutorialTimer <= 20) {
                mLayout.setText(mFont, "         Greetings, my little defender!\n       It's your old friend, mighty oak." +
                        "\nLord of Darkness wants to destroy me.\n   He ordered his monsters to hurt me.\n            Defender, I need your help.");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + 2.7f);
            } else if (mTutorialFlag == 1 && getDeadEnemies() == 0) {
                mLayout.setText(mFont, "    Throw in this goblin available weapons!\nClick on the weapon button on the button bar.");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + mLayout.height + 1.5f);
            } else if (mEnemies.size == 0 && getDeadEnemies() == 1) {
                mLayout.setText(mFont, "Excellent!");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + mLayout.height + 1.5f);
            }else if (mTutorialFlag == 2 && getDeadEnemies() == 1) {
                mLayout.setText(mFont, "          Look out! It's a zombie.\n    Some creatures are immune to \n        certain types of weapons.\n" +
                        "   For example, zombies cannot be \n        destroyed with shuriken.\nUse anything other than shuriken.");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + 5);
            } else if (mTutorialFlag == 2) {
                mLayout.setText(mFont, "        You can learn more about\nthe weaknesses of monsters from\n           the game encyclopedia.\n" +
                        "          To do this, click on the\n   book icon in the \"Achievements\"\n        section of the main menu.\n    Good job, defender. Good luck!");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + 5);
            }
        } else {
            if (mWaveStartTimer != 0 && mGameModeHelper.isSurviveMode()) {
                if(mWaveIndex % 5 == 0){
                    mLayout.setText(mFont, "             - W A V E  " + mWaveIndex + " - \nmonsters power increased");
                } else {
                    mLayout.setText(mFont, "- W A V E  " + mWaveIndex + " -");
                }
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + mLayout.height + 1.4f);
            } else if (mWaveStartTimer != 0 && mGameModeHelper.isStoryMode()) {
                mLayout.setText(mFont, "- L E V E L  " + mGameModeHelper.getLevel() + " -");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + mLayout.height + 1.4f);
            } else if (mWaveStartTimer != 0 && mGameModeHelper.isChallengeMode()) {
                mLayout.setText(mFont, "- C H A L L E N G E " + mGameModeHelper.getLevel() + " -");
                mFont.draw(pBatch, mLayout, ConstGame.X / 2 - mLayout.width / 2, ConstGame.Y / 2 + mLayout.height + 1.4f);
            }
        }
    }

    private void initTutorial() {
        if (mTutorialFlag == 0 && mTutorialTimer > 15) {
            spawn(new SpawnDef(new Vector2(ConstEnemies.SPAWN_LEFT, ConstEnemies.SPAWN_HEIGHT),
                    GoblinDefault.class, true));
            mTutorialFlag++;
            mTutorialTimer = 0;
        } else if (getDeadEnemies() == 1 && mTutorialFlag == 1 && mTutorialTimer > 3) {
            spawn(new SpawnDef(new Vector2(ConstEnemies.SPAWN_RIGHT, ConstEnemies.SPAWN_HEIGHT),
                    ZombieDefault.class, false));
            mTutorialFlag++;
            mTutorialTimer = 0;
        } else if (mTutorialFlag == 2 && getDeadEnemies() == 2 && mTutorialTimer > 15) {
            mPlayScreen.getOptions().setStart();
            mPlayScreen.setRestart(true);
            mPlayScreen.getGameGui().getGame().setScreen(new PlayScreen(mPlayScreen.getGameGui().getGame(), mPlayScreen.getType()));
            DefenderOfNature.musicManager.changeMusic(false);
        }
    }

    private boolean getSpawnPlace() {
        return MathUtils.randomBoolean();
    }

    private long getEnemySpawnDelay() {
        if (mPlayScreen.getType().contains(ConstGame.CHALLENGE_MODE)) {
            return MathUtils.random(1, 3);
        } else {
            return MathUtils.random(4, 8);
        }
    }

    public GameModeHelper getGameModeHelper() {
        return mGameModeHelper;
    }

    public int getWaveIndex() {
        return mWaveIndex;
    }

    @Override
    public void handleSpawning() {
        if (!mEnemiesToSpawn.isEmpty()) {
            final SpawnDef enemyDef = mEnemiesToSpawn.poll();
            switch (enemyDef.type.getSimpleName()) {
                case ConstEnemies.GOBLIN_DEFAULT:
                    mEnemies.add(new GoblinDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GOBLIN_MEDIUM:
                    mEnemies.add(new GoblinMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GOBLIN_HIGH:
                    mEnemies.add(new GoblinHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ORC_DEFAULT:
                    mEnemies.add(new OrcDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ORC_MEDIUM:
                    mEnemies.add(new OrcMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ORC_HIGH:
                    mEnemies.add(new OrcHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.TROLL_DEFAULT:
                    mEnemies.add(new TrollDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.TROLL_MEDIUM:
                    mEnemies.add(new TrollMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.TROLL_HIGH:
                    mEnemies.add(new TrollHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.EARWIG:
                    mEnemies.add(new Earwig(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.MOOSE:
                    mEnemies.add(new Moose(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ZOMBIE_DEFAULT:
                    mEnemies.add(new ZombieDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ZOMBIE_MEDIUM:
                    mEnemies.add(new ZombieMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ZOMBIE_HIGH:
                    mEnemies.add(new ZombieHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GARGOYLE_DEFAULT:
                    mEnemies.add(new GargoyleDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GARGOYLE_MEDIUM:
                    mEnemies.add(new GargoyleMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GARGOYLE_HIGH:
                    mEnemies.add(new GargoyleHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.MINOTAUR_DEFAULT:
                    mEnemies.add(new MinotaurDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.MINOTAUR_MEDIUM:
                    mEnemies.add(new MinotaurMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.MINOTAUR_HIGH:
                    mEnemies.add(new MinotaurHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.CYCLOP_DEFAULT:
                    mEnemies.add(new CyclopDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.CYCLOP_MEDIUM:
                    mEnemies.add(new CyclopMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.CYCLOP_HIGH:
                    mEnemies.add(new CyclopHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ELEMENTAL_DEFAULT:
                    mEnemies.add(new ElementalDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ELEMENTAL_MEDIUM:
                    mEnemies.add(new ElementalMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.ELEMENTAL_HIGH:
                    mEnemies.add(new ElementalHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.FLYING_DEFAULT:
                    mEnemies.add(new FlyingDefault(mPlayScreen, enemyDef.position.x, ConstEnemies.FLY_SPAWN_HEIGHT, enemyDef.direction));
                    break;
                case ConstEnemies.FLYING_HIGH:
                    mEnemies.add(new FlyingHigh(mPlayScreen, enemyDef.position.x, ConstEnemies.FLY_SPAWN_HEIGHT, enemyDef.direction));
                    break;
                case ConstEnemies.GOLEM_DEFAULT:
                    mEnemies.add(new GolemDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GOLEM_MEDIUM:
                    mEnemies.add(new GolemMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.GOLEM_HIGH:
                    mEnemies.add(new GolemHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.DRAGON_DEFAULT:
                    mEnemies.add(new DragonDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.DRAGON_MEDIUM:
                    mEnemies.add(new DragonMedium(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.STORM:
                    mEnemies.add(new Storm(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.YETI_DEFAULT:
                    mEnemies.add(new YetiDefault(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.YETI_MEDIUM:
                    mEnemies.add(new IcyFoot(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.YETI_HIGH:
                    mEnemies.add(new YetiHigh(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
                case ConstEnemies.KING_OF_THE_DEATH:
                    mEnemies.add(new KingOfTheDeath(mPlayScreen, enemyDef.position.x, enemyDef.position.y, enemyDef.direction));
                    break;
            }
        }
    }

    @Override
    public void spawn(final SpawnDef pSpawnDef) {
        mEnemiesToSpawn.add(pSpawnDef);
    }

    public void dispose() {
        mFont.dispose();
    }
}