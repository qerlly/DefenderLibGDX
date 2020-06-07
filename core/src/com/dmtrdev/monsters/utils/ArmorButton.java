package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dmtrdev.monsters.utils.ArmorTable;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.spawns.SpawnDef;
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

import java.util.Locale;

public class ArmorButton extends TextButton {

    public boolean flag;
    public final boolean available;
    private boolean mClicked;
    private float mTimer;
    private final TextButtonStyle mButtonStyle;
    private final int mId;
    private ArmorTable mArmorTable;
    private final Options mOptions;

    public ArmorButton(final String text, final PlayScreen pPlayScreen, final int pId, final boolean pAvailable) {
        super(text, pPlayScreen.getSkin());
        mId = pId;
        mTimer = 0;
        available = pAvailable;
        flag = mClicked = false;
        mOptions = pPlayScreen.getOptions();
        mButtonStyle = new TextButtonStyle();
        final TextButtonStyle buttonStyleOff = new TextButtonStyle();
        if (available) {
            buttonStyleOff.font = pPlayScreen.getSkin().getFont("default");
            buttonStyleOff.up = pPlayScreen.getSkin().getDrawable(pId + "_" + pId);
            mButtonStyle.up = pPlayScreen.getSkin().getDrawable(String.valueOf(pId));
        } else {
            mButtonStyle.up = pPlayScreen.getSkin().getDrawable("default");
        }
        mButtonStyle.font = pPlayScreen.getSkin().getFont("default");
        getLabel().setFontScale(0.025f);
        setStyle(mButtonStyle);
        addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (flag) {
                    mClicked = true;
                    setStyle(buttonStyleOff);
                    setTouchable(Touchable.disabled);
                    mArmorTable.getPlayScreen().getPlayer().setAttack();
                    mArmorTable.spawn(new SpawnDef(new Vector2(pPlayScreen.getPlayer().mBody.getPosition().x, pPlayScreen.getPlayer().mBody.getPosition().y),
                            getArmor(), pPlayScreen.getPlayer().getDirection()));
                }
            }
        });
        setText("");
    }

    public void playSound(final boolean flag) {
        if (mOptions.getSoundCheck()) {
            if (flag) {
                DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
            } else {
                DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
            }
        }
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (mClicked && ConstGame.GAME_STATE == ConstGame.State.PLAY) {
            mTimer -= delta;
            final String numberAsString = String.format(Locale.US, "%,.1f", mTimer);
            setText(numberAsString);
            if (mTimer <= 0) {
                mClicked = false;
                setStyle(mButtonStyle);
                setTouchable(Touchable.enabled);
                setText("");
            }
        }
    }

    public void setTable(final ArmorTable pArmorTable) {
        mArmorTable = pArmorTable;
    }

    public Class getArmor() {
        switch (mId) {
            case ConstArmor.TOMATO_ID:
                mTimer = ConstArmor.TOMATO_COOLDOWN;
                return Tomato.class;
            case ConstArmor.PUMPKIN_ID:
                mTimer = ConstArmor.PUMPKIN_COOLDOWN;
                return Pumpkin.class;
            case ConstArmor.SWEETCORN_ID:
                mTimer = ConstArmor.SWEETCORN_COOLDOWN;
                return SweetCorn.class;
            case ConstArmor.SPIKE_CLUB_ID:
                mTimer = ConstArmor.SPIKE_CLUB_COOLDOWN;
                return SpikeClub.class;
            case ConstArmor.STONE_AXE_ID:
                mTimer = ConstArmor.STONE_AXE_COOLDOWN;
                return StoneAxe.class;
            case ConstArmor.BARREL_ID:
                if(mArmorTable.getPlayScreen().getType().contains(ConstGame.CHALLENGE_MODE)){
                    mTimer = 2;
                }else {
                    mTimer = ConstArmor.BARREL_COOLDOWN;
                }
                return Barrel.class;
            case ConstArmor.ARROW_ID:
                mTimer = ConstArmor.ARROW_COOLDOWN;
                return Arrow.class;
            case ConstArmor.BONE_SYTHE_ID:
                mTimer = ConstArmor.BONE_SYTHE_COOLDOWN;
                return BoneSythe.class;
            case ConstArmor.BONE_AXE_ID:
                mTimer = ConstArmor.BONE_AXE_COOLDOWN;
                return BoneAxe.class;
            case ConstArmor.STORK_ID:
                mTimer = ConstArmor.STORK_COOLDOWN;
                return Stork.class;
            case ConstArmor.TREEBOMB_ID:
                mTimer = ConstArmor.TREEBOMB_COOLDOWN;
                return TreeBomb.class;
            case ConstArmor.MACE_ID:
                mTimer = ConstArmor.MACE_COOLDOWN;
                return Mace.class;
            case ConstArmor.STONEBOMB_ID:
                mTimer = ConstArmor.STONEBOMB_COOLDOWN;
                return StoneBomb.class;
            case ConstArmor.PRIMITIVE_AXE_ID:
                mTimer = ConstArmor.PRIMITIVE_AXE_COOLDOWN;
                return PrimitiveAxe.class;
            case ConstArmor.FLYING_BOMB_ID:
                mTimer = ConstArmor.FLYING_BOMB_COOLDOWN;
                return FlyingBomb.class;
            case ConstArmor.STANDARD_SHURIKEN_ID:
                mTimer = ConstArmor.STANDARD_SHURIKEN_COOLDOWN;
                return StandardShuriken.class;
            case ConstArmor.PRIMITIVE_SPEAR_ID:
                mTimer = ConstArmor.PRIMITIVE_SPEAR_COOLDOWN;
                return PrimitiveSpear.class;
            case ConstArmor.NINJA_KNIFE_ID:
                mTimer = ConstArmor.NINJA_KNIFE_COOLDOWN;
                return NinjaKnife.class;
            case ConstArmor.IRON_AXE_ID:
                mTimer = ConstArmor.IRON_AXE_COOLDOWN;
                return IronAxe.class;
            case ConstArmor.GRENADE_ID:
                mTimer = ConstArmor.GRENADE_COOLDOWN;
                return Grenade.class;
            case ConstArmor.PRIMITIVE_SHURIKEN_ID:
                mTimer = ConstArmor.PRIMITIVE_SHURIKEN_COOLDOWN;
                return PrimitiveShuriken.class;
            case ConstArmor.IRON_SPEAR_ID:
                mTimer = ConstArmor.IRON_SPEAR_COOLDOWN;
                return IronSpear.class;
            case ConstArmor.MODERN_SHURIKEN_ID:
                mTimer = ConstArmor.MODERN_SHURIKEN_COOLDOWN;
                return ModernShuriken.class;
            case ConstArmor.BOMB_ID:
                mTimer = ConstArmor.BOMB_COOLDOWN;
                return Bomb.class;
            case ConstArmor.THORN_ID:
                mTimer = ConstArmor.THORN_COOLDOWN;
                return Thorn.class;
            case ConstArmor.SPIKE_BOMB_ID:
                mTimer = ConstArmor.SPIKE_BOMB_COOLDOWN;
                return SpikeBomb.class;
            case ConstArmor.CARNIVORE_PLANT_ID:
                mTimer = ConstArmor.CARNIVORE_PLANT_COOLDOWN;
                return CarnivorePlant.class;
            case ConstArmor.GLOVE_ID:
                mTimer = ConstArmor.GLOVE_COOLDOWN;
                return Glove.class;
            case ConstArmor.TRAP_ID:
                mTimer = ConstArmor.TRAP_COOLDOWN;
                return Trap.class;
            case ConstArmor.PREHISTORIK_BOMB_ID:
                mTimer = ConstArmor.PREHISTORIK_BOMB_COOLDOWN;
                return PrehistorikBomb.class;
            case ConstArmor.BIRD_ID:
                mTimer = ConstArmor.BIRD_COOLDOWN;
                return Bird.class;
            case ConstArmor.BARREL_SHOT_ID:
                mTimer = ConstArmor.BARREL_SHOT_COOLDOWN;
                return BarrelShot.class;
            case ConstArmor.SPIKES_ID:
                mTimer = ConstArmor.SPIKES_COOLDOWN;
                return Spikes.class;
            case ConstArmor.MOLOTOV_ID:
                mTimer = ConstArmor.MOLOTOV_COOLDOWN;
                return Molotov.class;
            case ConstArmor.STONE_TRAP_ID:
                if(mArmorTable.getPlayScreen().getType().contains(ConstGame.CHALLENGE_MODE)){
                    mTimer = 4;
                }else {
                    mTimer = ConstArmor.STONE_TRAP_COOLDOWN;
                }
                return StoneTrap.class;
            case ConstArmor.AXE_SHOT_ID:
                mTimer = ConstArmor.AXE_SHOT_COOLDOWN;
                return AxeShot.class;
            case ConstArmor.ROBOREX_ID:
                mTimer = ConstArmor.ROBOREX_COOLDOWN;
                return RoboRex.class;
            default:
                mTimer = ConstArmor.TOMATO_COOLDOWN;
                return Tomato.class;
        }
    }
}