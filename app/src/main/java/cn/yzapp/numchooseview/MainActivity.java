package cn.yzapp.numchooseview;

import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockApplication;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.yzapp.numchooseviewlib.NumChooseView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NumChooseView numChooseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numChooseView = (NumChooseView) findViewById(R.id.num_view);

        numChooseView.setRootView(findViewById(R.id.rl_root));
        numChooseView.setCanShowHint(true);

        findViewById(R.id.ok).setOnClickListener(this);

        numChooseView.setOnNumChangeListener(new NumChooseView.OnNumChangeListener() {
            @Override
            public void onNumChangeListener(long num) {
                ((Button) findViewById(R.id.show_num)).setText("数量" + numChooseView.getBuyNum());
            }
        });

    }

    @Override
    public void onClick(View view) {
        numChooseView.setTerm(getNum(R.id.et_ShowStorage), getNum(R.id.et_LeastBuyNum), getNum(R.id.et_LimitNum), getNum(R.id.et_baseNum));
    }

    private int getNum(int res) {
        String num = ((EditText) findViewById(res)).getText().toString();
        if (TextUtils.isEmpty(num)) {
            return numChooseView.NOT_LIMIT;
        } else {
            return Integer.parseInt(num);
        }

    }

    public int test(){
        return 3;
    }
}
