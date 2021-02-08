package com.example.minimoneybox;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.minimoneybox.databinding.ActivityMainBinding;
import com.example.minimoneybox.network.data.ErrorBody;
import com.example.minimoneybox.view.ErrorFragmentArgs;
import com.example.minimoneybox.view.ui.MoneyBoxEventBroadcastReceiver;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MoneyBoxEventBroadcastReceiver.Listener {
    private ActivityMainBinding mBinding;

    @Inject
    MoneyBoxManager mMoneyBoxManager;

    private BroadcastReceiver mBroadcastReceiver;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        mBroadcastReceiver = new MoneyBoxEventBroadcastReceiver(this);
        registerReceiver(mBroadcastReceiver, new IntentFilter(MoneyBoxEventBroadcastReceiver.ACTION_HANDLE_EVENT));

        int startDestId = mMoneyBoxManager.isLoggedIn() ? R.id.userAccountFragment : R.id.loginFragment;
        setStartDestination(startDestId);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private void setStartDestination(int startDestId) {
        // Create new navigation graph
        NavGraph navGraph = navHostFragment.getNavController().getNavInflater().inflate(R.navigation.nav_graph);
        navGraph.setStartDestination(startDestId);

        // Replace the navigation controller's graph
        navHostFragment.getNavController().setGraph(navGraph);
    }

    @Override
    public void onAuthTokenExpired(ErrorBody errorBody) {
        mMoneyBoxManager.logout(false);
        navigateBackToLoginScreen();
        displaySessionExpiry(errorBody);
    }

    @Override
    public void onLogout() {
        navigateBackToLoginScreen();
    }

    private void displaySessionExpiry(ErrorBody errorBody) {
        navHostFragment.getNavController().popBackStack(R.id.loginFragment, false);

        String title = (errorBody == null) || (TextUtils.isEmpty(errorBody.getName())) ? getString(R.string.error_session_expired) : errorBody.getName();
        String message = (errorBody == null) || (TextUtils.isEmpty(errorBody.getMessage())) ? getString(R.string.error_login_again) : errorBody.getMessage();

        ErrorFragmentArgs args = new ErrorFragmentArgs.Builder(title, message).build();
        navHostFragment.getNavController().navigate(R.id.errorFragment, args.toBundle());
    }

    private void navigateBackToLoginScreen() {
        navHostFragment.getNavController().popBackStack(R.id.userAccountFragment, true);
        navHostFragment.getNavController().navigate(R.id.loginFragment);
    }
}
