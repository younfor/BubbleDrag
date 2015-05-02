package com.bubble;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.Toast;

public class BubbleMove extends View {

	String text="8";
	private int  curRadius;
	private Paint paint; // 绘制圆形图形
    private TextPaint textPaint; // 绘制圆形图形
    private Paint.FontMetrics textFontMetrics;
    private Point end;
    private Point base;
    private Point touch;
    Path path = new Path();
    private int moveRadius=20;
    private int maxDistance=150,curDistance=0;
    private boolean isMove=false;
    public BubbleMove(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void setRadius(int r)
    {
    	moveRadius=r;
    	curRadius=r;
    }
    public BubbleMove(Context context) {
        super(context);
        init(context);
    }
    public void setBasePoint(int x,int y)
    {
    	base=new Point(x, y);
    }
	public void init(Context context) {
		// 设置绘制flag的paint
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        // 设置绘制文字的paint
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(18);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textFontMetrics = textPaint.getFontMetrics();
        
        //初始坐标
        end=new Point((int)moveRadius,(int)moveRadius);
	}
	public void setText(String s)
	{
		this.text=s;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//背景透明
		canvas.drawColor(Color.TRANSPARENT);
		//画移动圆圈
		canvas.drawCircle(end.x+base.x, end.y+base.y, moveRadius, paint);
		
		//画贝塞尔曲线
		if(isMove&&curDistance<maxDistance)
		{
			canvas.drawCircle(base.x+moveRadius, base.y+moveRadius, curRadius, paint);
			path.reset();
            double sin = -1.0*(end.y-touch.y) / curDistance;
            double cos = 1.0*(end.x-touch.x) /curDistance ;
            // table圆上两点
            path.moveTo(
                    (float) (base.x+moveRadius - curRadius * sin),
                    (float) (base.y+moveRadius - curRadius * cos)
            );
            path.lineTo(
                    (float) (base.x+moveRadius + curRadius * sin),
                    (float) (base.y+moveRadius + curRadius * cos)
            );
            // move圆上两点
            path.quadTo(
                    (base.x+moveRadius + base.x+end.x) / 2, (base.y+moveRadius + base.y+end.y) / 2,
                    (float) (base.x+end.x +  moveRadius* sin), (float) (base.y+end.y + moveRadius * cos)
            );
            path.lineTo(
                    (float) (base.x+end.x - moveRadius * sin),
                    (float) (base.y+end.y- moveRadius * cos)
            );
            // 闭合
            path.quadTo(
            		 (base.x+moveRadius + base.x+end.x) / 2, (base.y+moveRadius +base.y+ end.y) / 2,
                    (float) (base.x+moveRadius - curRadius * sin), (float) (base.y+moveRadius - curRadius * cos)
            );
            canvas.drawPath(path, paint);
		}
		//移动圆上的文字
		float textH = - textFontMetrics.ascent - textFontMetrics.descent;
        canvas.drawText(text,end.x+base.x, end.y+base.y+ textH / 2, textPaint);

	}
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	touch=new Point((int)event.getX(),(int)event.getY());
            	isMove=true;
                break;
            case MotionEvent.ACTION_MOVE:
                end.x = (int)(event.getX());
                end.y = (int) (event.getY());
                double offx=event.getX()-touch.x;
                double offy=event.getY()-touch.y;
                //当前拉伸距离
                curDistance=(int)Math.sqrt(offx*offx+offy*offy);
                //定点圆随着距离变大而变小
                curRadius=(int)(moveRadius*(1.0-1.0*curDistance/maxDistance));
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            	isMove=false;
            	curRadius=moveRadius;
            	Point old=new Point(end);
                end=new Point((int)moveRadius,(int)moveRadius);
                postInvalidate();
                if(curDistance<maxDistance)
                {
                	Toast.makeText(getContext(), "松开了手", Toast.LENGTH_SHORT).show();
                	shakeAnimation(old);
                }
                else
                	Toast.makeText(getContext(), "用力过猛", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
	
	//CycleTimes动画重复的次数
    public void shakeAnimation(Point end) {
    	float x,y;
    	x=0.3f*(end.x-touch.x)*curDistance/maxDistance;
    	y=0.3f*(end.y-touch.y)*curDistance/maxDistance;
    	ObjectAnimator animx = ObjectAnimator .ofFloat(this, "translationX", x);
    	animx.setInterpolator(new CycleInterpolator(1));
    	ObjectAnimator animy = ObjectAnimator .ofFloat(this, "translationY", y);
    	animy.setInterpolator(new CycleInterpolator(1));
    	AnimatorSet set=new AnimatorSet();
    	set.setDuration(200);
    	set.playTogether(animx,animy);
    	set.start();

    }
	public void setMaxDistance(int dis)
	{
		maxDistance=dis;
	}
	
}
