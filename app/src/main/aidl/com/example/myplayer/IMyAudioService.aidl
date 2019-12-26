// IMyAudioService.aidl
package com.example.myplayer;

// Declare any non-default types here with import statements

interface IMyAudioService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    void openAudio(int position);

    void start();

    void pause();

    void next();

    void pre();

    void setPlayMode();

    int getPlayMode();

    int getCurrentPosition();

    int getDuration();

    String getAudioName();

    String getArtistName();

    void seekTo(int insert);

    boolean isPlaying();
}
