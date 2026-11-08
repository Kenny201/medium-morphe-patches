package app.medium.extension;

import android.app.Activity;
import android.util.Log;

@SuppressWarnings("unused")
public class MediumPatchExtension {
    private static final String TAG = "MediumPatchExtension";

    /**
     * Called when any Activity in Medium is created.
     */
    public static void onActivityCreated(Activity activity) {
        try {
            Log.d(TAG, "Medium activity created: " + activity.getClass().getName());
            TranslateOverlayManager.attachTranslateButton(activity);
        } catch (Throwable t) {
            Log.e(TAG, "Error in onActivityCreated hook", t);
        }
    }

    /**
     * Hook to translate a text string dynamically.
     */
    public static String translateText(String originalText) {
        if (originalText == null || originalText.length() < 10) {
            return originalText;
        }
        try {
            return MediumTranslator.translateText(originalText, MediumTranslator.getTargetLanguage());
        } catch (Throwable t) {
            Log.e(TAG, "Error translating text via extension", t);
            return originalText;
        }
    }

    /**
     * Hook to bypass paywall check.
     */
    public static boolean isArticleLocked(boolean originalStatus) {
        return false;
    }
}