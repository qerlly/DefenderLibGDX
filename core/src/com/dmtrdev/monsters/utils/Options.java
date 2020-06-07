package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Options {

    private final Preferences mOptions;

    public Options() {
        mOptions = Gdx.app.getPreferences("options");
    }

    //OPTIONS
    public boolean getMusicCheck() {
        return mOptions.getBoolean("music", true);
    }

    public void setMusicCheck(final boolean pCheck) {
        mOptions.putBoolean("music", pCheck);
        mOptions.flush();
    }

    public float getMusicVolume() {
        return mOptions.getFloat("music_volume", 1);
    }

    public void setMusicVolume(final float pMusicVolume) {
        if (pMusicVolume >= 0 && pMusicVolume <= 1) {
            mOptions.putFloat("music_volume", pMusicVolume);
            mOptions.flush();
        }
    }

    public float getSoundVolume() {
        return mOptions.getFloat("sound_volume", 1);
    }

    public void setSoundVolume(final float pSoundVolume) {
        if (pSoundVolume >= 0 && pSoundVolume <= 1) {
            mOptions.putFloat("sound_volume", pSoundVolume);
            mOptions.flush();
        }
    }

    public boolean getSoundCheck() {
        return mOptions.getBoolean("sound", true);
    }

    public void setSoundCheck(final boolean pCheck) {
        mOptions.putBoolean("sound", pCheck);
        mOptions.flush();
    }

    public boolean getEffectCheck() {
        return mOptions.getBoolean("effect", true);
    }

    public void setEffectCheck(final boolean pCheck) {
        mOptions.putBoolean("effect", pCheck);
        mOptions.flush();
    }

    //STATISTICS
    public float getCoins() {
        return mOptions.getFloat("coins", 0);
    }

    public void setCoins(final float pCoins) {
        if(getCoins() < 999) {
            mOptions.putFloat("coins", getCoins() + pCoins);
            mOptions.flush();
        }
    }

    public void setShopItems(final int pShopItems) {
        mOptions.putString("shopItems", getShopItems() + String.valueOf(pShopItems) + " ");
        mOptions.flush();
    }

    public String getShopItems() {
        return mOptions.getString("shopItems", "0 ");
    }

    public int getScore() {
        return mOptions.getInteger("score", 0);
    }

    public void setScore(final int pScore) {
        mOptions.putInteger("score", pScore);
        mOptions.flush();
    }

    public int getWavesCount() {
        return mOptions.getInteger("waves", 0);
    }

    public void setWavesCount(final int pWaves) {
        mOptions.putInteger("waves", pWaves);
        mOptions.flush();
    }

    public int getEnemiesCount() {
        return mOptions.getInteger("enemies", 0);
    }

    public void setEnemiesCount(final int pEnemies) {
        mOptions.putInteger("enemies", pEnemies);
        mOptions.flush();
    }

    public int getStoryLevel() {
        return mOptions.getInteger("levels", 0);
    }

    public void setStoryLevel(final int pLevels) {
        mOptions.putInteger("levels", pLevels);
        mOptions.flush();
    }

    public boolean firstStart(){
        return mOptions.getBoolean("tutorial", true);
    }

    public void setStart(){
        mOptions.putBoolean("tutorial", false);
        mOptions.flush();
    }

    public int getChallengesCount() {
        return mOptions.getInteger("challenges", 0);
    }

    public void setChallengesCount(final int pChallenges) {
        mOptions.putInteger("challenges", pChallenges);
        mOptions.flush();
    }

    public int getAvailableArmors() {
        return mOptions.getInteger("armors", 5 + (getStoryLevel() * 4));
    }
}