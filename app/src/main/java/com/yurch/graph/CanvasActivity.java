package com.yurch.graph;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class CanvasActivity extends AppCompatActivity {
	public static Intent newIntent(
			Context packageContext, float a, float b, float c, float d, float dx, float dy
	) {
		Intent intent = new Intent(packageContext, CanvasActivity.class);
		intent.putExtra("a", a);
		intent.putExtra("b", b);
		intent.putExtra("c", c);
		intent.putExtra("d", d);
		intent.putExtra("dx", dx);
		intent.putExtra("dy", dy);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(new CanvasView(
				this,
				intent.getFloatExtra("a", 1),
				intent.getFloatExtra("b", 1),
				intent.getFloatExtra("c", 1),
				intent.getFloatExtra("d", 1),
				intent.getFloatExtra("dx", 100),
				intent.getFloatExtra("dy", 100)
		));
	}
}