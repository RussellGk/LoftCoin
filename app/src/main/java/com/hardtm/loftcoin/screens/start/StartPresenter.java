package com.hardtm.loftcoin.screens.start;

public interface StartPresenter {

    void attechView(StartView view);

    void detachView();

    void loadRate();
}
