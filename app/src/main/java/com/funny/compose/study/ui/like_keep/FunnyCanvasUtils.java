package com.funny.compose.study.ui.like_keep;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Canvas;

public class FunnyCanvasUtils
{
	public static void drawCenterText(Canvas canvas,String text,float x,float y,Rect rect,Paint paint){
		paint.getTextBounds(text,0,text.length(),rect);
		Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=y+distance;
        canvas.drawText(text,x-rect.width()/2f,baseline,paint);
	}
	
	public static void drawCenterText(Canvas canvas,int text,float x,float y,Rect rect,Paint paint){
		drawCenterText(canvas,""+text,x,y,rect,paint);
	}
}
