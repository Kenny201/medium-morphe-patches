package app.medium.extension;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TranslateOverlayManager {
    private static final String TAG = "TranslateOverlayManager";
    private static final int BUTTON_ID = 0x7f099999;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void attachTranslateButton(final Activity activity) {
        if (activity == null) return;

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
                    if (rootView == null || rootView.findViewById(BUTTON_ID) != null) return;

                    TextView fab = new TextView(activity);
                    fab.setId(BUTTON_ID);
                    fab.setText("🌐 Перевести");
                    fab.setTextColor(Color.WHITE);
                    fab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    fab.setPadding(30, 16, 30, 16);
                    fab.setGravity(Gravity.CENTER);

                    GradientDrawable bg = new GradientDrawable();
                    bg.setColor(Color.parseColor("#008080"));
                    bg.setCornerRadius(50f);
                    fab.setBackground(bg);

                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.gravity = Gravity.BOTTOM | Gravity.END;
                    params.setMargins(0, 0, 40, 120);
                    fab.setLayoutParams(params);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onTranslateButtonClicked(activity);
                        }
                    });

                    rootView.addView(fab);
                    Log.d(TAG, "Translate button attached to: " + activity.getClass().getName());
                } catch (Exception e) {
                    Log.e(TAG, "Error attaching translate button", e);
                }
            }
        });
    }

    private static void onTranslateButtonClicked(final Activity activity) {
        Toast.makeText(activity, "Перевод статьи...", Toast.LENGTH_SHORT).show();

        List<TextView> textViews = new ArrayList<>();
        findTextViews(activity.getWindow().getDecorView(), textViews);

        if (textViews.isEmpty()) {
            Toast.makeText(activity, "Текст статьи не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (TextView tv : textViews) {
            sb.append(tv.getText().toString()).append("\n\n");
        }

        MediumTranslator.translateTextAsync(
            sb.toString(),
            MediumTranslator.getTargetLanguage(),
            new MediumTranslator.TranslationCallback() {
                @Override
                public void onSuccess(final String translatedText) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showTranslationDialog(activity, translatedText);
                        }
                    });
                }

                @Override
                public void onError(final Exception e) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        );
    }

    private static void findTextViews(View view, List<TextView> list) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            CharSequence cs = tv.getText();
            if (cs != null && cs.length() > 20) {
                list.add(tv);
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                findTextViews(group.getChildAt(i), list);
            }
        }
    }

    private static void showTranslationDialog(Activity activity, String translatedText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("🌐 Перевод статьи");

        ScrollView scrollView = new ScrollView(activity);
        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 30, 40, 30);

        TextView tv = new TextView(activity);
        tv.setText(translatedText);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setTextColor(Color.BLACK);
        tv.setLineSpacing(1.2f, 1.2f);
        container.addView(tv);

        scrollView.addView(container);
        builder.setView(scrollView);
        builder.setPositiveButton("Закрыть", null);
        builder.create().show();
    }
}