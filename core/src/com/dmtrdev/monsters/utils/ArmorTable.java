package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.spawns.SpawnDef;
import com.dmtrdev.monsters.spawns.SpawningSystem;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.sprites.armors.bombs.Bomb;
import com.dmtrdev.monsters.sprites.armors.bombs.Grenade;
import com.dmtrdev.monsters.sprites.armors.bombs.Molotov;
import com.dmtrdev.monsters.sprites.armors.bombs.PrehistorikBomb;
import com.dmtrdev.monsters.sprites.armors.bombs.SpikeBomb;
import com.dmtrdev.monsters.sprites.armors.bombs.StoneBomb;
import com.dmtrdev.monsters.sprites.armors.bombs.TreeBomb;
import com.dmtrdev.monsters.sprites.armors.flyings.Bird;
import com.dmtrdev.monsters.sprites.armors.flyings.FlyingBomb;
import com.dmtrdev.monsters.sprites.armors.flyings.Stork;
import com.dmtrdev.monsters.sprites.armors.ground.Barrel;
import com.dmtrdev.monsters.sprites.armors.ground.CarnivorePlant;
import com.dmtrdev.monsters.sprites.armors.ground.StoneTrap;
import com.dmtrdev.monsters.sprites.armors.ground.Thorn;
import com.dmtrdev.monsters.sprites.armors.ground.Trap;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.BoneAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.BoneSythe;
import com.dmtrdev.monsters.sprites.armors.medieval.IronAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.IronSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.Mace;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.SpikeClub;
import com.dmtrdev.monsters.sprites.armors.medieval.StoneAxe;
import com.dmtrdev.monsters.sprites.armors.ninja.ModernShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.NinjaKnife;
import com.dmtrdev.monsters.sprites.armors.ninja.PrimitiveShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.StandardShuriken;
import com.dmtrdev.monsters.sprites.armors.trees.AxeShot;
import com.dmtrdev.monsters.sprites.armors.trees.BarrelShot;
import com.dmtrdev.monsters.sprites.armors.trees.Glove;
import com.dmtrdev.monsters.sprites.armors.trees.RoboRex;
import com.dmtrdev.monsters.sprites.armors.trees.Spikes;
import com.dmtrdev.monsters.sprites.armors.vegetables.Pumpkin;
import com.dmtrdev.monsters.sprites.armors.vegetables.SweetCorn;
import com.dmtrdev.monsters.sprites.armors.vegetables.Tomato;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class ArmorTable extends Table implements Disposable, SpawningSystem {

    private float mTimer;
    private final Array<Armor> mArmors;
    private final BlockingDeque<SpawnDef> mArmorsToSpawn;
    private final PlayScreen mPlayScreen;
    private ArrayList<Integer> mAvailableArmors;

    public ArmorTable(final PlayScreen pPlayScreen, final int pAvailableArmors) {
        mPlayScreen = pPlayScreen;
        mTimer = 0;
        mArmors = new Array<Armor>();
        mArmorsToSpawn = new LinkedBlockingDeque<SpawnDef>();
        center();
        mAvailableArmors = new ArrayList<>();
        if(pAvailableArmors >= 28){
            for(int i = 1; i <= 28; i++){
                mAvailableArmors.add(i);
            }
        } else {
            for(int i = 1; i <= pAvailableArmors; i++){
                mAvailableArmors.add(i);
            }
        }
        Array<Integer> mas = new Array<>();
        mas.addAll(101, 102, 104, 105, 107, 108, 109, 110, 111);
        String[] buyMas = pPlayScreen.getOptions().getShopItems().split(" ");
        for (String buyMa : buyMas) {
            for (int j = 0; j < mas.size; j++) {
                if (buyMa.equals(String.valueOf(mas.get(j)))) {
                    mAvailableArmors.add(mas.get(j));
                }
            }
        }
    }

    public ArmorTable(final PlayScreen pPlayScreen, final int[] pLevels){
        mPlayScreen = pPlayScreen;
        mTimer = 0;
        mArmors = new Array<Armor>();
        mArmorsToSpawn = new LinkedBlockingDeque<SpawnDef>();
        center();

        for (int pLevel : pLevels) {
            final ArmorButton button = new ArmorButton("0.0f", mPlayScreen, pLevel, true);
            button.setTable(this);
            button.flag = true;
            add(button).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON).expand();
        }
    }

    private void addRandomArmor() {
        final int i;
        if(mPlayScreen.getGameGui().getGenerates().getWaveIndex() >= 5){
            i = mAvailableArmors.get(MathUtils.random(11, mAvailableArmors.size() - 1));
        } else {
            i = mAvailableArmors.get(MathUtils.random(mAvailableArmors.size() - 1));
        }
        final ArmorButton button = new ArmorButton("0.0f", mPlayScreen, i, true);
        button.setTable(this);
        button.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                getCell(button).reset();
                button.remove();
                spawn(new SpawnDef(new Vector2(mPlayScreen.getPlayer().mBody.getPosition().x, mPlayScreen.getPlayer().mBody.getPosition().y),
                        button.getArmor(), mPlayScreen.getPlayer().getDirection()));
            }
        });
        add(button).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON).expand();
    }

    public void update(final float delta) {
        handleSpawning();
        for (int i = 0; i < mArmors.size; i++) {
            if (mArmors.get(i).getDestroyed()) {
                mArmors.removeIndex(i);
                i--;
            } else {
                mArmors.get(i).update(delta);
            }
        }
        if (mPlayScreen.getType().equals(ConstGame.SURVIVE_MODE)) {
            if ((mTimer >= ConstGame.SURVIVE_ARMOR_TIME && mPlayScreen.getGameGui().getGenerates().getWaveIndex() <= 5 ||
                    mTimer >= 1.5f && mPlayScreen.getGameGui().getGenerates().getWaveIndex() > 5) && getChildren().size < 5) {
                addRandomArmor();
                mTimer = 0;
            } else {
                mTimer += delta;
            }
        }
    }

    public void drawArmors(final Batch pBatch) {
        for (final Armor armor : mArmors) {
            armor.draw(pBatch);
        }
    }

    @Override
    public void dispose() {
        mPlayScreen.getSkin().dispose();
    }

    @Override
    public void handleSpawning() {
        if (!mArmorsToSpawn.isEmpty()) {
            final SpawnDef armorDef = mArmorsToSpawn.poll();
            switch (armorDef.type.getSimpleName()) {
                case ConstArmor.TOMATO:
                    mArmors.add(new Tomato(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.PUMPKIN:
                    mArmors.add(new Pumpkin(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.SWEETCORN:
                    mArmors.add(new SweetCorn(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.SPIKE_CLUB:
                    mArmors.add(new SpikeClub(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.STONE_AXE:
                    mArmors.add(new StoneAxe(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BARREL:
                    mArmors.add(new Barrel(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.ARROW:
                    mArmors.add(new Arrow(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BONE_SYTHE:
                    mArmors.add(new BoneSythe(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BONE_AXE:
                    mArmors.add(new BoneAxe(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.STORK:
                    mArmors.add(new Stork(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.TREEBOMB:
                    mArmors.add(new TreeBomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.MACE:
                    mArmors.add(new Mace(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.STONEBOMB:
                    mArmors.add(new StoneBomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.PRIMITIVE_AXE:
                    mArmors.add(new PrimitiveAxe(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.STANDARD_SHURIKEN:
                    mArmors.add(new StandardShuriken(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.FLYING_BOMB:
                    mArmors.add(new FlyingBomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.PRIMITIVE_SPEAR:
                    mArmors.add(new PrimitiveSpear(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.NINJA_KNIFE:
                    mArmors.add(new NinjaKnife(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.IRON_AXE:
                    mArmors.add(new IronAxe(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.GRENADE:
                    mArmors.add(new Grenade(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.PRIMITIVE_SHURIKEN:
                    mArmors.add(new PrimitiveShuriken(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.IRON_SPEAR:
                    mArmors.add(new IronSpear(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.MODERN_SHURIKEN:
                    mArmors.add(new ModernShuriken(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BOMB:
                    mArmors.add(new Bomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.GLOVE:
                    mArmors.add(new Glove(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.THORN:
                    mArmors.add(new Thorn(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.SPIKE_BOMB:
                    mArmors.add(new SpikeBomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.CARNIVORE_PLANT:
                    mArmors.add(new CarnivorePlant(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.TRAP:
                    mArmors.add(new Trap(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.PREHISTORIK_BOMB:
                    mArmors.add(new PrehistorikBomb(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BIRD:
                    mArmors.add(new Bird(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.BARREL_SHOT:
                    mArmors.add(new BarrelShot(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.SPIKES:
                    mArmors.add(new Spikes(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.MOLOTOV:
                    mArmors.add(new Molotov(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.STONE_TRAP:
                    mArmors.add(new StoneTrap(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.AXE_SHOT:
                    mArmors.add(new AxeShot(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
                case ConstArmor.ROBOREX:
                    mArmors.add(new RoboRex(mPlayScreen, armorDef.position.x, armorDef.position.y, armorDef.direction));
                    break;
            }
        }
    }

    public PlayScreen getPlayScreen() {
        return mPlayScreen;
    }

    @Override
    public void spawn(final SpawnDef pSpawnDef) {
        mArmorsToSpawn.add(pSpawnDef);
    }
}