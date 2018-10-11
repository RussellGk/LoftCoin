package com.hardtm.loftcoin.screens.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class WelcomePage implements Parcelable {

    @DrawableRes
    private int imageIcon;
    @StringRes
    private int textTitle;
    @StringRes
    private int textSubTitle;

    public WelcomePage(int imageIcon, int textTitle, int textSubTitle) {
        this.imageIcon = imageIcon;
        this.textTitle = textTitle;
        this.textSubTitle = textSubTitle;
    }

    protected WelcomePage(Parcel in) {
        imageIcon = in.readInt();
        textTitle = in.readInt();
        textSubTitle = in.readInt();
    }

    public static final Creator<WelcomePage> CREATOR = new Creator<WelcomePage>() {
        @Override
        public WelcomePage createFromParcel(Parcel in) {
            return new WelcomePage(in);
        }

        @Override
        public WelcomePage[] newArray(int size) {
            return new WelcomePage[size];
        }
    };

    public int getImageIcon() {
        return imageIcon;
    }

    public int getTextTitle() {
        return textTitle;
    }

    public int getTextSubTitle() {
        return textSubTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageIcon);
        parcel.writeInt(textTitle);
        parcel.writeInt(textSubTitle);
    }
}
