package com.hardtm.loftcoin.screens.main.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.R;
import com.hardtm.loftcoin.data.api.Api;
import com.hardtm.loftcoin.data.db.Database;
import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.db.model.CoinEntityMapper;
import com.hardtm.loftcoin.data.prefs.Prefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RateFragment extends Fragment implements RateView {

    private static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_content)
    ViewGroup content;

    private RatePresenter presenter;
    private RateAdapter adapter;
    private Unbinder unbinder;
    private Parcelable layoutManagerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if(activity == null) {
            return;
        }

        Api api = ((App) getActivity().getApplication()).getApi();
        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();
        Database database = ((App)getActivity().getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();

        presenter = new RatePresenterImpl(api, prefs, database, mapper);
        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.rate_screen_title);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });
        if(savedInstanceState != null){
            layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }
        presenter.attechView(this);
        presenter.getRate();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LAYOUT_MANAGER_STATE, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {
        adapter.setCoins(coins);
        if(layoutManagerState != null) {
            recycler.getLayoutManager().onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }

    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {

    }
}
