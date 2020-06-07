package com.dmtrdev.monsters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dmtrdev.monsters.utils.PlayServices;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class GoogleServicesHelper implements PlayServices {

    private AndroidLauncher androidLauncher;
    static final int RC_SIGN_IN = 53245;
    private static final int RC_LEADERBOARD_UI = 7549;
    private byte i;
    private RewardedAd mRewardedAd;
    private InterstitialAd mInterstitialAd;
    private Boolean rewardBool;
    private int count;

    public GoogleServicesHelper(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        rewardBool = false;
        i = 0;
        count = 0;
    }

    @Override
    public void showReward() {
        if (isOnline()) {
            androidLauncher.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RewardedAd rewardedAd = mRewardedAd;
                    if (rewardedAd.isLoaded()) {
                        count = 0;
                        Activity activityContext = androidLauncher;
                        RewardedAdCallback adCallback = new RewardedAdCallback() {
                            @Override
                            public void onRewardedAdOpened() {
                            }

                            @Override
                            public void onRewardedAdClosed() {
                                loadReward();
                            }

                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem reward) {
                                rewardBool = true;
                            }

                            @Override
                            public void onRewardedAdFailedToShow(int errorCode) {
                            }
                        };
                        rewardedAd.show(activityContext, adCallback);
                    } else {
                        if (count < 1) {
                            loadReward();
                        }
                        count++;
                        if (count < 3) {
                            tostReward();
                        }
                    }
                }
            });
        } else {
            count = 0;
            tost();
        }
    }

    @Override
    public void loadReward() {
        RewardedAd rewardedAd = new RewardedAd(androidLauncher,
                "ca-app-pub-7315849484612233/9491833714");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        mRewardedAd = rewardedAd;
    }

    @Override
    public void loadInterstitial() {
        mInterstitialAd = new InterstitialAd(androidLauncher);
        mInterstitialAd.setAdUnitId("ca-app-pub-7315849484612233/8223355203");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void showInterstitial() {
        if (isOnlinesilently()) {
            androidLauncher.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        loadInterstitial();
                    }
                }
            });
        }
    }

    @Override
    public boolean getReward() {
        return rewardBool;
    }

    @Override
    public void setReward() {
        rewardBool = false;
    }

    @Override
    public void setIndex() {
        i = 0;
    }

    public void tost() {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(androidLauncher.getContext(), "Error, check your internet connection!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tostReward() {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(androidLauncher.getContext(), "Please wait a moment.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) androidLauncher.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        if (!isConnected) tost();
        return isConnected;
    }

    public boolean isOnlinesilently() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) androidLauncher.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }
        return isConnected;
    }

    @Override
    public boolean checkSignIn() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
        return GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray());
    }

    @Override
    public void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(androidLauncher,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        if (i < 1) {
            androidLauncher.startActivityForResult(intent, RC_SIGN_IN);
            i++;
        }
    }

    @Override
    public void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            GoogleSignInAccount signedInAccount = account;
        } else {
            GoogleSignInClient signInClient = GoogleSignIn.getClient(androidLauncher, signInOptions);
            signInClient
                    .silentSignIn()
                    .addOnCompleteListener(
                            androidLauncher,
                            new OnCompleteListener<GoogleSignInAccount>() {
                                @Override
                                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                    if (task.isSuccessful()) {
                                        GoogleSignInAccount signedInAccount = task.getResult();
                                        String message = "Google Play: Authorization completed!";
                                        new AlertDialog.Builder(androidLauncher.getContext()).setMessage(message)
                                                .setNeutralButton(android.R.string.ok, null).show();
                                    } else {
                                        startSignInIntent();
                                    }
                                }
                            });
        }
    }

    @Override
    public void submitLeaderboard(final int pScore) {
        Games.getLeaderboardsClient(androidLauncher, GoogleSignIn.getLastSignedInAccount(androidLauncher))
                .submitScore(androidLauncher.getString(R.string.leaderboard_monsters_leaderboards), pScore);
    }

    @Override
    public void getLeaderboard() {
        Games.getLeaderboardsClient(androidLauncher, GoogleSignIn.getLastSignedInAccount(androidLauncher))
                .getLeaderboardIntent(androidLauncher.getString(R.string.leaderboard_monsters_leaderboards))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        androidLauncher.startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    @Override
    public void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(androidLauncher,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnFailureListener(androidLauncher, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                String message = "Error, check your internet connection!";
                new AlertDialog.Builder(androidLauncher.getContext()).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        });

        signInClient.signOut().addOnCompleteListener(androidLauncher,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String message = "You logged out!";
                        new AlertDialog.Builder(androidLauncher.getContext()).setMessage(message)
                                .setNeutralButton(android.R.string.ok, null).show();
                    }
                });
    }
}
