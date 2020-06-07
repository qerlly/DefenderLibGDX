package com.dmtrdev.monsters.utils;

public interface PlayServices {
    public void startSignInIntent();
    public void signOut();
    public void signInSilently();
    public void submitLeaderboard(final int pScore);
    public void getLeaderboard();
    public void setIndex();
    public boolean checkSignIn();
    public boolean isOnline();
    public void showReward();
    public void loadReward();
    public boolean getReward();
    public void setReward();
    public void loadInterstitial();
    public void showInterstitial();
}
