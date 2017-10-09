package im.shimo.react.prompt;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.graphics.Color;
import javax.annotation.Nullable;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.Html;
import android.text.SpannableString;
import android.view.WindowManager;



public class RNPromptFragment extends DialogFragment implements DialogInterface.OnClickListener {

    /* package */ static final String ARG_TITLE = "title";
    /* package */ static final String ARG_MESSAGE = "message";
    /* package */ static final String ARG_BUTTON_POSITIVE = "button_positive";
    /* package */ static final String ARG_BUTTON_NEGATIVE = "button_negative";
    /* package */ static final String ARG_BUTTON_NEUTRAL = "button_neutral";
    /* package */ static final String ARG_ITEMS = "items";
    /* package */ static final String ARG_TYPE = "type";
    /* package */ static final String ARG_DEFAULT_VALUE = "defaultValue";
    /* package */ static final String ARG_PLACEHOLDER = "placeholder";

    private EditText mInputText;

    public enum PromptTypes {
        TYPE_DEFAULT("default"),
        PLAIN_TEXT("plain-text"),
        SECURE_TEXT("secure-text");

        private final String mName;

        PromptTypes(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    private final @Nullable RNPromptModule.PromptFragmentListener mListener;

    public RNPromptFragment() {
        mListener = null;
    }

    public RNPromptFragment(@Nullable RNPromptModule.PromptFragmentListener listener, Bundle arguments) {
        mListener = listener;
        setArguments(arguments);
    }


    public static Dialog createDialog(
            Context activityContext, Bundle arguments, RNPromptFragment fragment) {
              int colorId = Color.parseColor("#009fe3");
              String title = arguments.getString(ARG_TITLE);
              String formStart = "<big><font color='Black'>";
              String formEnd = "</font></big>";
              String formattedTitle = formStart + title + formEnd;
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext)
        .setTitle(Html.fromHtml(formattedTitle));

        if (arguments.containsKey(ARG_BUTTON_POSITIVE)) {
            builder.setPositiveButton(arguments.getString(ARG_BUTTON_POSITIVE), fragment);
        }
        if (arguments.containsKey(ARG_BUTTON_NEGATIVE)) {
            builder.setNegativeButton(arguments.getString(ARG_BUTTON_NEGATIVE), fragment);
        }
        if (arguments.containsKey(ARG_BUTTON_NEUTRAL)) {
            builder.setNeutralButton(arguments.getString(ARG_BUTTON_NEUTRAL), fragment);
        }
        // if both message and items are set, Android will only show the message
        // and ignore the items argument entirely
        String subTitle = arguments.getString(ARG_MESSAGE);
        String subTitleStart = "<br /><br /><font color='Gray'>";
        String subTitleEnd = "</font></span>";
        String formattedSubTitle = subTitleStart + subTitle + subTitleEnd;
        if (arguments.containsKey(ARG_MESSAGE)) {
            builder.setMessage(Html.fromHtml(formattedSubTitle));
        }

        if (arguments.containsKey(ARG_ITEMS)) {
            builder.setItems(arguments.getCharSequenceArray(ARG_ITEMS), fragment);
        }

        AlertDialog alertDialog = builder.create();

        // Set up the input
        final EditText input = new EditText(activityContext);

        int type = InputType.TYPE_CLASS_TEXT;
        String typeString = arguments.getString(ARG_TYPE);

        if (typeString == null) {
            typeString = "plain-text";
        }

        if (arguments.containsKey(ARG_TYPE)) {
            switch (typeString) {
                case "secure-text":
                    type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    break;
                case "plain-text":
                default:
                    type = InputType.TYPE_CLASS_TEXT;
            }
        }
        input.setInputType(type);
        fragment.setTextInput(input);

        if (arguments.containsKey(ARG_DEFAULT_VALUE)) {
            String defaultValue = arguments.getString(ARG_DEFAULT_VALUE);
            if (defaultValue != null) {
                input.setText(defaultValue);
                int textLength = input.getText().length();
                input.setSelection(textLength, textLength);
                input.getPaint().setColor(colorId);
            }
        }

        if (arguments.containsKey(ARG_PLACEHOLDER)) {
            input.setHint(arguments.getString(ARG_PLACEHOLDER));
        }
      //  builder.setTitle(arguments.getString(ARG_TITLE));
        alertDialog.setView(input, 50, 40, 40, 50);

        return alertDialog;
    }

    public void setTextInput(EditText input) {
        mInputText = input;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = createDialog(getActivity(), getArguments(), this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mListener != null) {
            mListener.onConfirm(which, mInputText.getText().toString());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDismiss(dialog);
        }
    }
}
