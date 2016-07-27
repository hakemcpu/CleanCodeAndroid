package test.com.cleancodesample.presentation.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import test.com.cleancodesample.R;
import test.com.cleancodesample.presentation.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        MainFragment fragment = (MainFragment) manager.findFragmentById(R.id.container);
        if(fragment == null) {
            fragment = MainFragment.newInstance();
            manager.beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void onEvent(String msg) {
        Toast.makeText(this, "Received '"+msg+"'", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
