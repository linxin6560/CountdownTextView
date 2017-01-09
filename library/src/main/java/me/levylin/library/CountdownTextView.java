package me.levylin.library;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 倒计时控件
 *
 * @author LinXin
 * @date 2014年12月15日 下午2:51:14
 */
public class CountdownTextView extends TextView {
    /**
     * 倒数间隔,默认为1000
     */
    private final static int COUNT_DOWN_INTERVAL = 1000;
    private Date mDate;
    private SimpleDateFormat mDateFormat;
    private OnFinishListener listener;
    private boolean isCountDowning = false;//是否在倒计时中
    private CountDownTimer mCountDownTimer;

    public CountdownTextView(Context context) {
        super(context);
        init();
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mDate = new Date();
    }

    public void setFormat(String format) {
        mDateFormat = new SimpleDateFormat(format, Locale.CHINA);
    }

    /**
     * 开始倒计时
     *
     * @param millisInFuture 毫秒级剩余时间
     */
    public void start(final long millisInFuture) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        setTimeText(millisInFuture);
        mCountDownTimer = new CountDownTimer(millisInFuture, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTimeText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                setTimeText(0);
                if (listener != null) {
                    listener.onCountFinish(CountdownTextView.this);
                }
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 停止倒计时
     */
    public void stop() {
        isCountDowning = false;
        mCountDownTimer.cancel();
    }

    private void setTimeText(long millisInFuture) {
        isCountDowning = true;
        mDate.setTime(millisInFuture);
        String text;
        if (mDateFormat != null) {
            text = mDateFormat.format(mDate);
        } else {
            text = String.valueOf(millisInFuture / 1000);
        }
        setText(text);
    }

    /**
     * 是否在倒计时中
     *
     * @return true:在倒计时中，false，不在倒计时
     */
    public boolean isCountDowning() {
        return isCountDowning;
    }

    public void setCustomText(CharSequence text) {
        isCountDowning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        setText(text);
    }

    public void setCustomText(int text) {
        isCountDowning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        setText(text);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isCountDowning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public interface OnFinishListener {
        void onCountFinish(View view);
    }
}
