package com.hardtm.loftcoin.screens.welcome;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardtm.loftcoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WelcomeFragment extends Fragment {

    private static final String KEY_PAGE = "page";

    public static WelcomeFragment newInstance(WelcomePage page) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PAGE, page);
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public WelcomeFragment() {

    }

    @BindView(R.id.icon)
    ImageView imageIcon;
    @BindView(R.id.title)
    TextView textTitle;
    @BindView(R.id.subtitle)
    TextView textSubtitle;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if( args != null) {
            WelcomePage page = args.getParcelable(KEY_PAGE);
            if(page != null) {
                imageIcon.setImageResource(page.getImageIcon());
                textTitle.setText(page.getTextTitle());
                textSubtitle.setText(page.getTextSubTitle());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
