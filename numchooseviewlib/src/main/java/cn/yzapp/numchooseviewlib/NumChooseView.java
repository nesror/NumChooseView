package cn.yzapp.numchooseviewlib;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
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
    public final static String TAG = "NumChooseView";
    public final static int NOT_LIMIT = -1;
    private NumBean mNumBean;
    private long mGoodNum = 1;
    private ImageView numAdd;
    private EditText tvNum;
    private ImageView numLes;
    private boolean showToast;
    private boolean rootViewCheck;
    private String mMsgLimit = "不能超过限购数量！";
    private String mMsgShowStorage = "不能超过库存数量！";
    private String mMsgLeastBuyNum = "不能少于起购数量！";
    private String mMsgBasicNum = "购买数量必须为整箱件数的倍数！";
    private IImeListener mImeListener;
    private boolean isChecked;

    public NumChooseView(Context context) {
        super(context);
        initView();
    }

    public NumChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        setAttrs(context, attrs);

    }

    public NumChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumChooseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.numchoose_CardEditTextView,
                0, 0);
        try {
            int textColor = a.getColor(R.styleable.numchoose_CardEditTextView_numchoose_textColor, 0x666666);
            float textSize = a.getDimension(R.styleable.numchoose_CardEditTextView_numchoose_textSize, 13);
            tvNum.setTextColor(textColor);
            tvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } finally {
            a.recycle();
        }
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

    /**
     * 设置提示语
     *
     * @param msgLimit       不能超过限购数量
     * @param msgShowStorage 不能超过库存数量
     * @param msgLeastBuyNum 不能少于起购数量
     * @param msgBasicNum    购买数量必须为整箱件数的倍数
     */
    public void setMsg(String msgLimit, String msgShowStorage, String msgLeastBuyNum, String msgBasicNum) {
        if (!TextUtils.isEmpty(msgLimit)) mMsgLimit = msgLimit;
        if (!TextUtils.isEmpty(msgLimit)) mMsgShowStorage = msgShowStorage;
        if (!TextUtils.isEmpty(msgLimit)) mMsgLeastBuyNum = msgLeastBuyNum;
        if (!TextUtils.isEmpty(msgLimit)) mMsgBasicNum = msgBasicNum;
    }

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

    /**
     * 设置键盘状态监听
     */
    public void setOnImeListener(IImeListener imeListener) {
        mImeListener = imeListener;
    }

    /**
     * 校验数量
     */
    public boolean checkNum() {
        return numCheck();
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
                        // 不能超过限购数量
                        numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                        tvNum.setText((mGoodNum < mNumBean.getLimitNum() ? mGoodNum : mNumBean.getLimitNum()) + "");
                        showToast(getContext(), mMsgLimit);
                        tvNum.setSelection(tvNum.length());
                        return;
                    }

                    if (mNumBean.getShowStorage() != NOT_LIMIT && Long.parseLong(editable.toString()) > mNumBean.getShowStorage()) {
                        // 不能超过库存数量
                        numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                        tvNum.setText((mGoodNum < mNumBean.getShowStorage() ? mGoodNum : mNumBean.getShowStorage()) + "");
                        showToast(getContext(), mMsgShowStorage);
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
            hideSoftInput();

            if (mNumBean.getLeastBuyNum() == NOT_LIMIT || (mGoodNum - mNumBean.getBasicNum()) >= (mNumBean.getLeastBuyNum() == 0 ? 1 : mNumBean.getLeastBuyNum())) {
                mGoodNum -= mNumBean.getBasicNum();
                tvNum.setText("" + mGoodNum);
            } else {
                // "不能少于起购数量！"
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                showToast(getContext(), mMsgLeastBuyNum);
            }
        } else if (view.getId() == R.id.num_add) {
            hideSoftInput();

            if (mNumBean.getLimitNum() == NOT_LIMIT || ((mGoodNum + mNumBean.getBasicNum()) <=
                    mNumBean.getLimitNum())) {
                if (mNumBean.getShowStorage() == NOT_LIMIT || mGoodNum < mNumBean.getShowStorage()) {
                    mGoodNum += mNumBean.getBasicNum();
                    tvNum.setText("" + mGoodNum);
                    numLes.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // "不能超过库存数量！"
                    numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                    showToast(getContext(), mMsgShowStorage);
                }
            } else {
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                // "不能超过限购数量！";
                showToast(getContext(), mMsgLimit);
            }
        }
    }

    private void hideSoftInput() {
        try {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(tvNum.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    private void setRootViewCheck(final View rootView) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                        if (!rootViewCheck && heightDiff < dip2px(getContext(), 200)) { // 键盘收起
                            rootViewCheck = true;
                            if (mImeListener != null) {
                                mImeListener.onImeListener(false);
                            }
                            if (TextUtils.isEmpty(tvNum.getText().toString())) {
                                tvNum.setText(mGoodNum + "");
                                tvNum.setSelection(tvNum.length());
                                return;
                            }
                            if (mNumBean.getBasicNum() != 1 && (Long.parseLong(tvNum.getText().toString()) % mNumBean.getBasicNum()) != 0) {
                                // "购买数量必须为整箱瓶数的倍数！"
                                showToast(getContext(), mMsgBasicNum);
                                tvNum.setText(mGoodNum + "");
                                tvNum.setSelection(tvNum.length());
                            } else if ((mNumBean.getLeastBuyNum() != -1 && Long.parseLong(tvNum.getText().toString()) < mNumBean.getLeastBuyNum())) {
                                //不能小于起购数量－置灰
                                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                                showToast(getContext(), mMsgLeastBuyNum);
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
                            if (mImeListener != null) {
                                mImeListener.onImeListener(true);
                            }
                            rootViewCheck = false;
                        }
                    }
                });
    }

    private void showToast(Context ct, String s) {
        if (showToast && isChecked) {
            ToastUtil.shortToast(ct, s);
        }
    }

    private void check() {
        if (mNumBean.getLimitNum() != NOT_LIMIT && (mNumBean.getLimitNum() < mNumBean.getLeastBuyNum())) {
            Log.w(TAG, "最大购买数量不能小于最低购买数量！");
            tvNum.setText(mNumBean.getLimitNum() + "");
            mGoodNum = mNumBean.getLimitNum();
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
            numLes.setEnabled(false);
            numAdd.setEnabled(false);
            tvNum.setEnabled(false);
            showToast = false;
            isChecked = true;
            return;
        }

        if (mNumBean.getBasicNum() > mNumBean.getLeastBuyNum()) {
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            tvNum.setText(mNumBean.getBasicNum() + "");
            mGoodNum = mNumBean.getBasicNum();
        } else if (mNumBean.getLeastBuyNum() % mNumBean.getBasicNum() != 0) {
            long num = mNumBean.getBasicNum() * (mNumBean.getLeastBuyNum() / mNumBean.getBasicNum() + 1);

            if (mNumBean.getLimitNum() != NOT_LIMIT && (mNumBean.getLimitNum() < num)) {
                // 购买数量大于限购数量
                tvNum.setText(mNumBean.getLimitNum() + "");
                mGoodNum = mNumBean.getLimitNum();
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                numLes.setEnabled(false);
                numAdd.setEnabled(false);
                tvNum.setEnabled(false);
                showToast = false;
                isChecked = true;
                return;
            }
            if (mNumBean.getShowStorage() != NOT_LIMIT && (mNumBean.getShowStorage() < num)) {
                // 购买数量大于库存数量
                tvNum.setText(mNumBean.getShowStorage() + "");
                mGoodNum = mNumBean.getShowStorage();
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
                numLes.setEnabled(false);
                numAdd.setEnabled(false);
                tvNum.setEnabled(false);
                showToast = false;
            } else {
                numLes.setBackgroundResource(R.color.numchoose_bg_gray);
                tvNum.setText(num + "");
                mGoodNum = num;
            }

        }

        if (mNumBean.getShowStorage() != NOT_LIMIT && (mNumBean.getShowStorage() < mNumBean.getLeastBuyNum() || mNumBean.getShowStorage() < mNumBean.getBasicNum())) {
            // 库存数量小于最低购买数量或购买基数
            tvNum.setText(mNumBean.getShowStorage() + "");
            mGoodNum = mNumBean.getShowStorage();
            numLes.setBackgroundResource(R.color.numchoose_bg_gray);
            numAdd.setBackgroundResource(R.color.numchoose_bg_gray);
            numLes.setEnabled(false);
            numAdd.setEnabled(false);
            tvNum.setEnabled(false);
            showToast = false;
        }

        isChecked = true;

    }

    private int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public interface IImeListener {
        void onImeListener(boolean open);
    }

    private boolean numCheck() {
        if (getShowNum() < 1) {
            showToast(getContext(), "请输入购买数量");
            return false;
        }

        if (mNumBean.getLimitNum() > 0 && mNumBean.getLimitNum() != 9999 &&
                getShowNum() > mNumBean.getLimitNum()) {
            showToast(getContext(), mMsgLimit);
            return false;
        }

        if (getShowNum() > mNumBean.getShowStorage()) {
            showToast(getContext(), mMsgShowStorage);
            return false;
        }

        if (mNumBean.getLeastBuyNum() > 0 && getShowNum() < mNumBean.getLeastBuyNum()) {
            showToast(getContext(), mMsgLeastBuyNum);
            return false;
        }

        if (mNumBean.getBasicNum() > 1 && (getShowNum() % mNumBean.getBasicNum()) != 0) {
            showToast(getContext(), mMsgBasicNum);
            return false;
        }

        return true;
    }

}
