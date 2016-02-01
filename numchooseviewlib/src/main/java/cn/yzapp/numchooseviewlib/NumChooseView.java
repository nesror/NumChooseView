package cn.yzapp.numchooseviewlib;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.yzapp.numchooseviewlib.model.NumBean;

/**
 * @author: nestor
 * time: 1/22 022-13:58.
 * email: nestor@yzapp.cn
 * desc:
 */
public class NumChooseView extends LinearLayout implements View.OnClickListener {
    public final static int NOT_LIMIT = -1;
    private NumBean mNumBean;
    private long mGoodNum = 1;
    private ImageView numAdd;
    private EditText tvNum;
    private ImageView numLes;
    private static boolean showToast;

    public NumChooseView(Context context) {
        super(context);
        initView();
    }

    public NumChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NumChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumChooseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mNumBean = new NumBean();
        View numView = View.inflate(getContext(), R.layout.include_view_num_choose, null);
        tvNum = (EditText) numView.findViewById(R.id.tv_num);
        numLes = (ImageView) numView.findViewById(R.id.num_les);
        numAdd = (ImageView) numView.findViewById(R.id.num_add);

        tvNum.addTextChangedListener(new NumTextWatcher(getContext(), mNumBean, mGoodNum, tvNum, numLes, numAdd));
        numLes.setOnClickListener(this);
        numAdd.setOnClickListener(this);

        addView(numView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.num_les) {
            if (mNumBean.getLeastBuyNum() == -1 || mGoodNum > (mNumBean.getLeastBuyNum() == 0 ? 1 : mNumBean.getLeastBuyNum())) {
                mGoodNum--;
                tvNum.setText("" + mGoodNum);
            } else {
                numLes.setBackgroundResource(R.color.bg_gray);
                showToast(getContext(), "不能少于起购数量！");
            }
        } else {
            if (mNumBean.getLimitNum() == -1 || mGoodNum < mNumBean.getLimitNum()) {
                if (mNumBean.getShowStorage() == -1 || mGoodNum < mNumBean.getShowStorage()) {
                    mGoodNum++;
                    tvNum.setText("" + mGoodNum);
                    numLes.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    numAdd.setBackgroundResource(R.color.bg_gray);
                    showToast(getContext(), "不能超过库存数量！");
                }
            } else {
                numAdd.setBackgroundResource(R.color.bg_gray);
                showToast(getContext(), "不能超过限购数量！");
            }
        }
    }

    /**
     * 得到数量
     */
    public long getBuyNum() {
        return mGoodNum;
    }

    /**
     * 设置库存
     *
     * @param showStorage 库存
     */
    public void setShowStorage(long showStorage) {
        mNumBean.setShowStorage(showStorage);
    }

    /**
     * 设置最少购买数量
     *
     * @param buyNum 最少购买数量
     */
    public void setLeastBuyNum(long buyNum) {
        if (buyNum < 1) {
            buyNum = 1;
        }
        mGoodNum = buyNum;
        tvNum.setText("" + mGoodNum);
        mNumBean.setLeastBuyNum(buyNum);
    }

    /**
     * 设置限购数量
     *
     * @param limitNum 限购数量
     */
    public void setLimitNum(long limitNum) {
        mNumBean.setLimitNum(limitNum);
    }

    /**
     * 设置根视图，用于监听键盘是否收起
     *
     * @param rootView 最少购买数量
     */
    public void setRootView(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                        if (heightDiff < 300) { // 键盘收起
                            //不能小于起购数量－置灰
                            if (mNumBean.getLeastBuyNum() != -1 && (TextUtils.isEmpty(tvNum.getText().toString()) || Long.parseLong(tvNum.getText().toString()) < mNumBean.getLeastBuyNum())) {
                                numLes.setBackgroundResource(R.color.bg_gray);
                                showToast(getContext(), "不能少于起购数量！");
                                if (mNumBean.getLeastBuyNum() != 0) {
                                    tvNum.setText(mNumBean.getLeastBuyNum() + "");
                                    mGoodNum = mNumBean.getLeastBuyNum();
                                } else {
                                    tvNum.setText("1");
                                    mGoodNum = 1;
                                }
                                tvNum.setSelection(tvNum.length());
                            }
                        }
                    }
                });
    }


    /**
     * 设置是否显示提示
     *
     */
    public void setCanShowHint(boolean showToast) {
        NumChooseView.showToast = showToast;
    }


    public static void showToast(Context ct, String s) {
        if (showToast) {
            Toast.makeText(ct, s, Toast.LENGTH_SHORT).show();
        }
    }

}
