package cn.yzapp.numchooseviewlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.yzapp.numchooseviewlib.model.NumBean;

/**
 * @author: nestor
 * time: 1/22 022-13:58.
 * email: nestor@yzapp.cn
 * desc:
 */
public class NumTextWatcher implements TextWatcher {
    private final EditText tvNum;
    private final ImageView numLes;
    private final ImageView numAdd;
    private final Context mContext;
    private NumBean mNumBean;
    private long mGoodNum;

    public NumTextWatcher(Context context, NumBean numBean, long goodNum, EditText tvNum, ImageView numLes, ImageView numAdd) {
        this.mContext = context;
        this.mNumBean = numBean;
        this.mGoodNum = goodNum;
        this.tvNum = tvNum;
        this.numLes = numLes;
        this.numAdd = numAdd;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        if (mNumBean.getShowStorage() < mNumBean.getLeastBuyNum() || mNumBean.getLimitNum() < mNumBean.getLeastBuyNum()) {
            return;
        }
        //删除数量－置灰
        if (s.length() < 1) {
            numLes.setBackgroundResource(R.color.bg_gray);
            mGoodNum = -1;
            tvNum.setSelection(tvNum.length());
            return;
        }

        if (mNumBean != null) {
            if (mNumBean.getLimitNum() != -1 && Long.parseLong(s.toString()) > mNumBean.getLimitNum()) {
                numAdd.setBackgroundResource(R.color.bg_gray);
                tvNum.setText(mNumBean.getLimitNum() + "");
                NumChooseView.showToast(mContext, "不能超过限购数量！");
                tvNum.setSelection(tvNum.length());
                mGoodNum = mNumBean.getLimitNum();
                return;
            }

            if (mNumBean.getShowStorage() != -1 && Long.parseLong(s.toString()) > mNumBean.getShowStorage()) {
                numAdd.setBackgroundResource(R.color.bg_gray);
                tvNum.setText(mNumBean.getShowStorage() + "");
                NumChooseView.showToast(mContext, "不能超过库存数量！");
                tvNum.setSelection(tvNum.length());
                mGoodNum = mNumBean.getShowStorage();
                return;
            }
        }
        mGoodNum = Long.parseLong(s.toString());
        if (mGoodNum <= 1 || (mNumBean.getLeastBuyNum() != -1 && mGoodNum <= mNumBean.getLeastBuyNum())) {
            numLes.setBackgroundResource(R.color.bg_gray);
        } else {
            numLes.setBackgroundColor(Color.TRANSPARENT);
        }

        if ((mNumBean.getLimitNum() != -1 && mGoodNum >= mNumBean.getLimitNum()) || (mNumBean.getShowStorage() != -1 && mGoodNum >= mNumBean.getShowStorage())) {
            numAdd.setBackgroundResource(R.color.bg_gray);
        } else {
            numAdd.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
