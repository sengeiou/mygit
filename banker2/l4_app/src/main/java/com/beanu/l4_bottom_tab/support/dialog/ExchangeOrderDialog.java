package com.beanu.l4_bottom_tab.support.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;

public class ExchangeOrderDialog extends Dialog {
    public ExchangeOrderDialog(Context context) {
        super(context);
    }

    public ExchangeOrderDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private float textSize;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private EditText exchangeCode;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        private Builder setMessage(String msg) {
            this.message = msg;
            return this;
        }

        public Builder setMessage(String message, float textSize) {
            this.message = message;
            this.textSize = textSize;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }



        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public String getExchangeCode() {
            return exchangeCode.getText().toString().trim();
        }



        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }


        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public ExchangeOrderDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ExchangeOrderDialog dialog = new ExchangeOrderDialog(context,R.style.Dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);


            View layout = inflater.inflate(R.layout.dialog_exchange_netorder, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);

            exchangeCode =  (EditText)layout.findViewById(R.id.edit_exchange);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.submit))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.submit))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.submit).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.cancel))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.cancel).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                TextView msgView = (TextView) layout.findViewById(R.id.message);
                msgView.setText(message);
                if (textSize != 0) {
                    msgView.setTextSize(textSize);
                }
            } else if (contentView != null) {

            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
            }



