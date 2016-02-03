package cn.yzapp.numchooseviewlib;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.yzapp.numchooseviewlib.model.NumBean;
import cn.yzapp.numchooseviewlib.util.ToastUtil;

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
    private boolean showToast;

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

    /**
     * 设置限制条件
     *
     * @param showStorage 库存
     * @param leastbuyNum 最少购买数量
     * @param limitNum    限购数量
     * @param basicNum    购买基数
     */
    public void setTerm(long showStorage, long leastbuyNum, long limitNum, long basicNum) {
        setShowStorage(showStorage);
        setLeastBuyNum(leastbuyNum);
        setLimitNum(limitNum);
        setBasicNum(basicNum);
        check();
    }

    boolean rootViewCheck;

    /**
     * 设置根视图，用于监听键盘是否收起
     *
     * @param rootView 最少购买数量
     */
    public void setRootView(final View rootView) {
        setRootViewCheck(rootView);
    }

    /**
     * 设置是否显示提示
     */
    public void setCanShowHint(boolean showToast) {
        this.showToast = showToast;
    }

    /**
     * 得到数量
     */
    public long getBuyNum() {
        return mGoodNum;
    }

    /**
     * 得到显示数量
     */
    public long getShowNum() {
        if (TextUtils.isEmpty(tvNum.getText().toString())) {
            return 0;
        }
        return Long.parseLong(tvNum.getText().toString());
    }

    private void setShowStorage(long showStorage) {
        mNumBean.setShowStorage(showStorage);
    }

    private void setLeastBuyNum(long buyNum) {
        if (buyNum < 1) {
            buyNum = 1;
        }
        mNumBean.setLeastBuyNum(buyNum);
        mGoodNum = buyNum;
        tvNum.setText("" + mGoodNum);
    }

    private void setLimitNum(long limitNum) {
        mNumBean.setLimitNum(limitNum);
    }

    private void setBasicNum(long basicNum) {
        if (basicNum < 1) {
            basicNum = 1;
        }
        mNumBean.setBasicNum(basicNum);
    }

    private void initView() {
        mNumBean = new NumBean();
        View numView = View.inflate(getContext(), R.layout.numchoose_include_view_num_choose, null);
        tvNum = (EditText) numView.findViewById(R.id.tv_num);
        numLes = (ImageView) numView.findViewById(R.id.num_les);
        numAdd = (ImageView) numView.findViewById(R.id.num_add);
        tvNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //删除数量－置灰
                if (editable.length() < 1) {
                    numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                    tvNum.setSelection(tvNum.length());
                    return;
                }

                if (mNumBean != null) {
                    if (mNumBean.getLimitNum() != NOT_LIMIT && Long.parseLong(editable.toString()) > mNumBean.getLimitNum()) {
                        numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                        tvNum.setText((mGoodNum < mNumBean.getLimitNum() ? mGoodNum : mNumBean.getLimitNum()) + "");
                        showToast(getContext(), "不能超过限购数量！");
                        tvNum.setSelection(tvNum.length());
                        return;
                    }

                    if (mNumBean.getShowStorage() != NOT_LIMIT && Long.parseLong(editable.toString()) > mNumBean.getShowStorage()) {
                        numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                        tvNum.setText((mGoodNum < mNumBean.getShowStorage() ? mGoodNum : mNumBean.getShowStorage()) + "");
                        showToast(getContext(), "不能超过库存数量！");
                        tvNum.setSelection(tvNum.length());
                        return;
                    }
                }
                if (mNumBean.getBasicNum() == 1 || (Long.parseLong(editable.toString()) % mNumBean.getBasicNum()) == 0) {
                    if (Long.parseLong(editable.toString()) >= mNumBean.getLeastBuyNum()) {
                        mGoodNum = Long.parseLong(editable.toString());
                    }
                }
                if (mGoodNum <= 1 || (mNumBean.getLeastBuyNum() != NOT_LIMIT && mGoodNum <= mNumBean.getLeastBuyNum())) {
                    numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                } else {
                    numLes.setBackgroundColor(Color.TRANSPARENT);
                }

                if ((mNumBean.getLimitNum() != NOT_LIMIT && mGoodNum >= mNumBean.getLimitNum()) || (mNumBean.getShowStorage() != NOT_LIMIT && mGoodNum >= mNumBean.getShowStorage())) {
                    numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                } else {
                    numAdd.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
        numLes.setOnClickListener(this);
        numAdd.setOnClickListener(this);

        addView(numView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.num_les) {
            if (mNumBean.getLeastBuyNum() == NOT_LIMIT || (mGoodNum - mNumBean.getBasicNum()) >= (mNumBean.getLeastBuyNum() == 0 ? 1 : mNumBean.getLeastBuyNum())) {
                mGoodNum -= mNumBean.getBasicNum();
                tvNum.setText("" + mGoodNum);
            } else {
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                showToast(getContext(), "不能少于起购数量！");
            }
        } else if (view.getId() == R.id.num_add) {
            if (mNumBean.getLimitNum() == NOT_LIMIT || ((mGoodNum + mNumBean.getBasicNum()) <=
                    mNumBean.getLimitNum())) {
                if (mNumBean.getShowStorage() == NOT_LIMIT || mGoodNum < mNumBean.getShowStorage()) {
                    mGoodNum += mNumBean.getBasicNum();
                    tvNum.setText("" + mGoodNum);
                    numLes.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                    showToast(getContext(), "不能超过库存数量！");
                }
            } else {
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                showToast(getContext(), "不能超过限购数量！");
            }
        }
    }

    private void setRootViewCheck(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                        if (!rootViewCheck && heightDiff < 300) { // 键盘收起
                            rootViewCheck = true;
                            if (TextUtils.isEmpty(tvNum.getText().toString())) {
                                tvNum.setText(mGoodNum + "");
                                tvNum.setSelection(tvNum.length());
                                return;
                            }
                            if (mNumBean.getBasicNum() != 1 && (Long.parseLong(tvNum.getText().toString()) % mNumBean.getBasicNum()) != 0) {
                                showToast(getContext(), "必须是购买基数的整数倍！");
                                tvNum.setText(mGoodNum + "");
                                tvNum.setSelection(tvNum.length());
                            } else if ((mNumBean.getLeastBuyNum() != -1 && Long.parseLong(tvNum.getText().toString()) < mNumBean.getLeastBuyNum())) {
                                //不能小于起购数量－置灰
                                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                                showToast(getContext(), "不能少于起购数量！");
                                tvNum.setText(mGoodNum + "");
                                /*if (mNumBean.getLeastBuyNum() != 0) {
                                    tvNum.setText(mNumBean.getLeastBuyNum() + "");
                                    mGoodNum = mNumBean.getLeastBuyNum();
                                } else {
                                    tvNum.setText("1");
                                    mGoodNum = 1;
                                }*/
                                tvNum.setSelection(tvNum.length());
                            }
                        } else {
                            rootViewCheck = false;
                        }
                    }
                });
    }

    private void showToast(Context ct, String s) {
        if (showToast) {
            ToastUtil.shortToast(ct, s);
        }
    }

    private void check() {
        if (mNumBean.getLimitNum() != NOT_LIMIT && (mNumBean.getLimitNum() < mNumBean.getLeastBuyNum())) {
            showToast(getContext(), "最大购买数量不能小于最低购买数量！");
            tvNum.setText(mNumBean.getLimitNum() + "");
            mGoodNum = mNumBean.getLimitNum();
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
            numLes.setEnabled(false);
            numAdd.setEnabled(false);
            tvNum.setEnabled(false);
            return;
        }

        if (mNumBean.getBasicNum() > mNumBean.getLeastBuyNum()) {
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            tvNum.setText(mNumBean.getBasicNum() + "");
            mGoodNum = mNumBean.getBasicNum();
        } else if (mNumBean.getLeastBuyNum() % mNumBean.getBasicNum() != 0) {
            long num = mNumBean.getBasicNum() * (mNumBean.getLeastBuyNum() / mNumBean.getBasicNum() + 1);

            if (mNumBean.getLimitNum() != NOT_LIMIT && (mNumBean.getLimitNum() < num)) {
                tvNum.setText(mNumBean.getLimitNum() + "");
                mGoodNum = mNumBean.getLimitNum();
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                numLes.setEnabled(false);
                numAdd.setEnabled(false);
                tvNum.setEnabled(false);
                return;
            }
            if (mNumBean.getShowStorage() != NOT_LIMIT && (mNumBean.getShowStorage() < num)) {
                tvNum.setText(mNumBean.getShowStorage() + "");
                mGoodNum = mNumBean.getShowStorage();
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                numLes.setEnabled(false);
                numAdd.setEnabled(false);
                tvNum.setEnabled(false);
            } else {
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                tvNum.setText(num + "");
                mGoodNum = num;
            }

        }

        if (mNumBean.getShowStorage() != NOT_LIMIT && (mNumBean.getShowStorage() < mNumBean.getLeastBuyNum() || mNumBean.getShowStorage() < mNumBean.getBasicNum())) {
            tvNum.setText(mNumBean.getShowStorage() + "");
            mGoodNum = mNumBean.getShowStorage();
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
            numLes.setEnabled(false);
            numAdd.setEnabled(false);
            tvNum.setEnabled(false);
        }

    }

}
