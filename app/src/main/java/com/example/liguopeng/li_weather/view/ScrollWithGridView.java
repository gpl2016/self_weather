package com.example.liguopeng.li_weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by fySpring
 * Date : 2017/4/24
 * To do :
 */

public class ScrollWithGridView extends GridView {
    public ScrollWithGridView(Context context) {
        super(context);
    }

    public ScrollWithGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWithGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
