package me.levylin.library;

import android.content.Context;
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
public class CountdownTextView extends TextView implements Runnable {
    /**
     * 倒数间隔,默认为1000
     */
    private final static int COUNT_DOWN_INTERVAL = 1000;
    //结束时间
    private long millisInFuture;
    private Date mDate;
    private SimpleDateFormat mDateFormat;
    private OnFinishListener listener;
    private boolean isCountDowning = false;//是否在倒计时中

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
    public void start(long millisInFuture) {
        isCountDowning = true;
        this.millisInFuture = millisInFuture;
        mDate.setTime(millisInFuture);
        String text = mDateFormat.format(mDate);
        setText(text);
        if (millisInFuture >= 0) {
            removeCallbacks(this);
            postDelayed(this, COUNT_DOWN_INTERVAL);
        }
    }

    /**
     * 停止倒计时
     */
    public void stop() {
        isCountDowning = false;
        removeCallbacks(this);
    }

    /**
     * 是否在倒计时中
     *
     * @return true:在倒计时中，false，不在倒计时
     */
    public boolean isCountDowning() {
        return isCountDowning;
    }

    @Override
    public void run() {
        if (!isCountDowning)
            return;
        millisInFuture -= COUNT_DOWN_INTERVAL;
        if (millisInFuture < 0) {
            millisInFuture = 0;
            if (listener != null) {
                listener.onCountFinish(this);
            }
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                start(millisInFuture);
            }
        });
    }

    public void setCustomText(CharSequence text) {
        isCountDowning = false;
        removeCallbacks(this);
        setText(text);
    }

    public void setCustomText(int text) {
        isCountDowning = false;
        removeCallbacks(this);
        setText(text);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isCountDowning = false;
        removeCallbacks(this);
    }

    public void setOnFinishListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public interface OnFinishListener {
        void onCountFinish(View view);
    }
}
