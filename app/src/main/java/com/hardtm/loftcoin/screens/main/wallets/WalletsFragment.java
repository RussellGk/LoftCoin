package com.hardtm.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hardtm.loftcoin.App;
import com.hardtm.loftcoin.R;
import com.hardtm.loftcoin.data.db.model.CoinEntity;
import com.hardtm.loftcoin.data.prefs.Prefs;
import com.hardtm.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.hardtm.loftcoin.screens.currencies.CurrenciesBottomSheetListener;
import com.hardtm.loftcoin.screens.main.wallets.adapters.WalletsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    public static final String TAG = "WalletsFragment";

    private static final String VIEW_PAGER_POS = "view_pager_pos";

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    private WalletsViewModelImpl viewModel;
    private WalletsPagerAdapter walletsPagerAdapter;
    private Integer restoreViewPagerPos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
        Prefs prefs = ((App)getActivity().getApplication()).getPrefs();
        walletsPagerAdapter = new WalletsPagerAdapter(prefs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.wallets_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);
        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.spacing_240);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.spacing_20);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;
        walletsPager.setPageMargin(-pageMargin);
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);
        Fragment bottomSheet = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if(bottomSheet != null) {
            ((CurrenciesBottomSheet) bottomSheet).setListener(this);
        }
        viewModel.getWallets();
        initOutputs();
        initInputs();
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());
        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(menuItem -> {
            viewModel.onNewWalletClick();
            return true;
        });
    }

    private void initInputs() {
        viewModel.wallets().observe(this, wallets -> {
            walletsPagerAdapter.setWallets(wallets);
        });

        viewModel.walletsVisible().observe(this, visible -> walletsPager.setVisibility(visible ? View.VISIBLE:View.GONE));
        viewModel.newWalletVisible().observe(this, visible -> newWallet.setVisibility(visible ? View.VISIBLE:View.GONE));

        viewModel.selectCurrency().observe(this, o -> {
            showCurrenciesBottomSheet();
        });
    }

    private void showCurrenciesBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }
}
