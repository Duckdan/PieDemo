package study.com.piedemo.weight;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import study.com.piedemo.DataBean;

/**
 * Created by Administrator on 2018/3/2.
 */

public class PieChartView extends View {

    private float halfWidth;
    private float halfHeight;
    private List<DataBean> list;
    private float totalValue = 0f;
    private Paint piePaint;
    private Paint linePaint;
    private Path path;
    private RectF rectf;
    private float radius;
    //角度范围的数组
    private float[] angleArrays;
    //当前点击的位置
    private int clickPosition = -1;
    private RectF clickRectF;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        piePaint = new Paint();
        piePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5);
        linePaint.setColor(Color.BLACK);
        linePaint.setTextSize(30);
        path = new Path();
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<DataBean> list) {
        this.list = list;
        calculate();
        angleArrays = new float[list.size()];
    }

    /**
     * 计算
     */
    private void calculate() {
        for (int i = 0; i < list.size(); i++) {
            totalValue += list.get(i).getValue();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        halfWidth = w * 1.0f / 2;
        halfHeight = h * 1.0f / 2;
        radius = Math.min(halfWidth, halfHeight) * 0.6f;
        rectf = new RectF(-radius, -radius, radius, radius);

        clickRectF = new RectF(-radius - 15, -radius - 15, radius + 15, radius + 15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float startAngle = 0f;
        float sweepAngle = 0f;
        float lineStartX = 0f;
        float lineStartY = 0f;
        float lineEndX = 0f;
        float lineEndY = 0f;
        float lineAngle = 0f;
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            DataBean dataBean = list.get(i);
            path.moveTo(0, 0); //起始点移动至圆点
            sweepAngle = dataBean.getValue() * 360 / totalValue - 1;
            if (i == clickPosition) {
                path.arcTo(clickRectF, startAngle, sweepAngle);
            } else {
                path.arcTo(rectf, startAngle, sweepAngle);
            }
            piePaint.setColor(Color.parseColor(dataBean.getColor()));
            canvas.drawPath(path, piePaint);
            lineAngle = startAngle + sweepAngle / 2;
            lineStartX = (float) (radius * Math.cos(Math.toRadians(lineAngle)));
            lineStartY = (float) (radius * Math.sin(Math.toRadians(lineAngle)));
            lineEndX = (float) ((radius + 30) * Math.cos(Math.toRadians(lineAngle)));
            lineEndY = (float) ((radius + 30) * Math.sin(Math.toRadians(lineAngle)));
            str = String.format("%.1f", dataBean.getValue() / totalValue * 100) + "%";
            if (lineAngle >= 90 && lineAngle <= 270) {
                linePaint.setTextAlign(Paint.Align.RIGHT);
            } else {
                linePaint.setTextAlign(Paint.Align.LEFT);
            }
            canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, linePaint);
            canvas.drawText(str, lineEndX, lineEndY, linePaint);

            angleArrays[i] = startAngle % 360;  //取余数
            startAngle += sweepAngle + 1;
            path.reset(); //重置path，清空path的配置
        }
        canvas.restore();
    }

    private float x;
    private float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX(); //获取相对于控件自身的x坐标
                y = event.getY(); //获取相对于控制自身的y坐标
                x = x - halfWidth; //转换坐标
                y = y - halfHeight;
                double sqrt = Math.sqrt(x * x + y * y);
                if (sqrt <= radius) {  //必须点击在圆饼状之内
                    Log.e("position::", y + "=0=" + x);
                    float transformAngle = transformAngle(x, y); //转化的角度
                    //二分查找,如果寻找不到则选择查询值最近的较大值下标,并取其值加1之后取结果负值返回
                    int searchIndex = Arrays.binarySearch(angleArrays, transformAngle);
                    Log.e("position::", transformAngle + "=1=" + searchIndex);
                    clickPosition = searchIndex >= 0 ? searchIndex : (-searchIndex - 2);
                    Log.e("position::", clickPosition + "=2=");
                    invalidate(); //重绘
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    //转化角度
    public float transformAngle(float x, float y) {
        float transAngle = (float) Math.toDegrees(Math.atan2(y, x));  //计算得出的值的范围为[-π/2,π/2];
        return transAngle >= 0 ? transAngle : transAngle + 360;
    }
}
