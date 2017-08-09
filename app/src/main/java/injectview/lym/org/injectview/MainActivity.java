package injectview.lym.org.injectview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BindLayout;
import com.example.BindView;
import com.example.OnClick;

import injectview.lym.org.injectview_api.InjectView;

/**
 *
 * @author yaoming.li
 * @since 2017-08-08 17:51
 */
@BindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_inject)
    TextView mTv;
    @BindView(R.id.tv_inject_two)
    TextView mTvTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectView.inject(this);
        mTv.setText("thi is inject textView");
    }

    @OnClick({R.id.tv_inject_two,R.id.tv_inject})
    public void show(){
        Toast.makeText(this,"this is inject onclick listener",Toast.LENGTH_SHORT).show();
    }

}
