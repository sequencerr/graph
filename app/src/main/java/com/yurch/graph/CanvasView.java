package com.yurch.graph;


import static java.lang.Math.abs;
import static java.lang.Math.pow;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


class CanvasView extends View {
	final float a;
	final float b;
	final float c;
	final float d;
	final float dx;
	final float dy;

	public CanvasView(
			Context context,
			float aP,
			float bP,
			float cP,
			float dP,
			float dxP,
			float dyP
	) {
		super(context);
		a = aP;
		b = bP;
		c = cP;
		d = dP;
		dx = dxP;
		dy = dyP;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) ==
				Configuration.UI_MODE_NIGHT_YES) {
			paint.setColor(Color.LTGRAY);
		}
		else {
			paint.setColor(Color.BLACK);
		}

		Paint paint2 = new Paint();
		paint2.setColor(Color.BLUE);
		Paint paint3 = new Paint();
		paint3.setColor(Color.RED);

		int w = getWidth(); // x
		int h = getHeight(); // y
		int wHalf = w >> 1;
		int hHalf = h >> 1;

		// draw coordinate plane
		canvas.drawLine(0, hHalf, w, hHalf, paint);
		canvas.drawLine(wHalf, 0, wHalf, h, paint);
		// arrows
		// x axis arrow
		canvas.drawLine(w, hHalf, w - (w >> 5), hHalf + (h >> 5), paint);
		canvas.drawLine(w, hHalf, w - (w >> 5), hHalf - (h >> 5), paint);
		// y axis arrow
		canvas.drawLine(wHalf, 0, wHalf + (w >> 5), (h >> 5), paint);
		canvas.drawLine(wHalf, 0, wHalf - (w >> 5), (h >> 5), paint);

		// previous calculations
		float px = 0;
		float py = 0;

		boolean stopOnNextIteration = false;
		boolean rightHalfCompleted = false;
		final int i0 = 0;
		int i = i0;
		while (true) {
			// i = 0.6, i += 0.1, 0.70000005 problem https://ideone.com/Ef23yz
			float x = ((float) i) / 10;
			float y = a * ((float) pow(x, 3)) + b * (float) pow(x, 2) + c * x + d;

			boolean isOutsideBorder =
					hHalf - (y * dy) < 0
							|| y > h
							|| wHalf + (x * dx) < 0
							|| x > w;

			if (!rightHalfCompleted) {
				if (!isOutsideBorder) {
					if (i == i0) {
						px = x;
						py = y;
						i++;
						continue;
					}
					i++;
				}
				else {
					if (!stopOnNextIteration) {
						stopOnNextIteration = true;
					}
					else {
						stopOnNextIteration = false;
						rightHalfCompleted = true;
						i = i0;
						continue;
					}
				}
			}
			else {
				if (!isOutsideBorder) {
					if (i == i0) {
						px = x;
						py = y;
						i--;
						continue;
					}
					i--;
				}
				else {
					if (!stopOnNextIteration) {
						stopOnNextIteration = true;
					}
					else {
						break;
					}
				}
			}

			// TODO: fix dx dy
			canvas.drawLine(
					wHalf + (px * dx), hHalf - (py * dy), wHalf + (x * dx),
					hHalf - (y * dy),
					paint
			);

			// TODO: x == wHalf || y == hHalf
			if (abs(x) == 1) {
				canvas.drawCircle(wHalf + (px * dx), hHalf - (py * dy), 5, paint3);
			}
			else {
				canvas.drawCircle(wHalf + (px * dx), hHalf - (py * dy), 5, paint2);
			}

			px = x;
			py = y;
		}
	}
}