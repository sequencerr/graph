package com.yurch.graph;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
	float a;
	float b;
	float c;
	float d;
	float dx = 100;
	float dy = 100;
	Button buildButton;
	EditText editText;
	EditText editText1;
	EditText editText2;
	EditText editText3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		editText = findViewById(R.id.editTextNumberDecimalA);
		editText1 = findViewById(R.id.editTextNumberDecimalB);
		editText2 = findViewById(R.id.editTextNumberDecimalC);
		editText3 = findViewById(R.id.editTextNumberDecimalD);

		TextView textView = findViewById(R.id.displayVariableA);
		TextView textView1 = findViewById(R.id.displayVariableB);
		TextView textView2 = findViewById(R.id.displayVariableC);
		TextView textView3 = findViewById(R.id.displayVariableD);

		editText.addTextChangedListener(createTextWatcher(textView, Coefficient.A));
		editText1.addTextChangedListener(createTextWatcher(textView1, Coefficient.B));
		editText2.addTextChangedListener(createTextWatcher(textView2, Coefficient.C));
		editText3.addTextChangedListener(createTextWatcher(textView3, Coefficient.D));

		editText.setOnFocusChangeListener(createFocusListener());
		editText1.setOnFocusChangeListener(createFocusListener());
		editText2.setOnFocusChangeListener(createFocusListener());
		editText3.setOnFocusChangeListener(createFocusListener());

		EditText editText4 = findViewById(R.id.editTextNumberDecimalDx);
		EditText editText5 = findViewById(R.id.editTextNumberDecimalDy);

		editText4.addTextChangedListener(createTextWatcherDxDy(Diff.Dx));
		editText5.addTextChangedListener(createTextWatcherDxDy(Diff.Dy));

		editText4.setOnFocusChangeListener(createFocusListener());
		editText5.setOnFocusChangeListener(createFocusListener());

		buildButton = findViewById(R.id.button);
		buildButton.setOnClickListener(
				v -> startActivity(CanvasActivity.newIntent(this, a, b, c, d, dx, dy)));
	}

	private TextWatcher createTextWatcher(TextView v, Coefficient coefficient) {
		return new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable e) {
				String s = e.toString();

				if (s.equals(".")) {
					e.replace(0, e.length(), "0.");
					return;
				}
				if (s.equals("-.")) {
					e.replace(0, e.length(), "-0.");
					return;
				}
				if (s.equals("0")) {
					e.replace(0, e.length(), "");
					return;
				}
				// TODO: -0
				// TODO: available 0
				// TODO: limits -10000 10000

				float coefficientNum;
				if (s.length() == 0) {
					coefficientNum = 1;
				}
				else if (s.equals("-")) {
					coefficientNum = -1;
				}
				else {
					coefficientNum = Float.parseFloat(s);
				}

				char coefficientChar;
				switch (coefficient) {
				case A:
					coefficientChar = 'a';
					a = coefficientNum;
					break;
				case B:
					coefficientChar = 'b';
					b = coefficientNum;
					break;
				case C:
					coefficientChar = 'c';
					c = coefficientNum;
					break;
				case D:
					coefficientChar = 'd';
					d = coefficientNum;
					break;
				default:
					throw new Error("Wrong character: " + coefficient);
				}

				v.setText(v.getId() == R.id.displayVariableA
				          ? (s.length() == 0
				             ? ((Character) coefficientChar).toString()
				             : (s.equals("-") || s.equals("-1"))
				               ? "-"
				               : s.equals("1")
				                 ? ""
				                 : s)
				          : (s.length() == 0
				             ? "+" + coefficientChar
				             : (s.equals("-") || s.equals("-1"))
				               ? v.getId() == R.id.displayVariableD
				                 ? "-1"
				                 : "-"
				               : s.equals("1")
				                 ? v.getId() == R.id.displayVariableD
				                   ? "+1"
				                   : "+"
				                 : !s.startsWith("-")
				                   ? "+" + s
				                   : s));

				buildButton.setEnabled(!s.endsWith(".") && editText.getText().length() != 0 &&
						editText1.getText().length() != 0 && editText2.getText().length() != 0 &&
						editText3.getText().length() != 0);
			}
		};
	}

	private TextWatcher createTextWatcherDxDy(Diff d) {
		return new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable e) {
				String s = e.toString();

				if (s.equals(".")) {
					e.replace(0, e.length(), "0.");
					return;
				}
				if (s.equals("0")) {
					e.replace(0, e.length(), "");
					return;
				}

				float v = s.length() == 0
				                ? 100
				                : Float.parseFloat(s);
				switch (d) {
				case Dx:
					dx = v;
					break;
				case Dy:
					dy = v;
					break;
				}
			}
		};
	}

	private View.OnFocusChangeListener createFocusListener() {
		return (v, hasFocus) -> {
			EditText e = (EditText) v;
			if (!hasFocus) {
				String s = e.getText().toString();
				if (s.equals("-")) {
					e.setText("-1");
					return;
				}
				if (s.endsWith(".")) {
					e.getText().delete(s.length() - 1, s.length());
				}
				// TODO: cut in .13200000 (e.g.) zeros
			}
		};
	}

	enum Coefficient {
		A, B, C, D
	}

	enum Diff {
		Dx, Dy
	}
}