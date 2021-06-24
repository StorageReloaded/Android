package io.github.storagereloaded.android.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final int STATUS_OK = 0;
    public static final int STATUS_NO_SERVER = 1;
    public static final int STATUS_WRONG_CREDENTIALS = 2;

    TextInputLayout serverAddress;
    TextInputLayout username;
    TextInputLayout password;

    LinearLayout infoLayout;
    TextView infoText;
    LinearLayout errorLayout;
    TextView errorText;

    LoginViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        model = new ViewModelProvider(this).get(LoginViewModel.class);

        serverAddress = findViewById(R.id.server_address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        infoLayout = findViewById(R.id.info_layout);
        infoText = findViewById(R.id.info_text);
        errorLayout = findViewById(R.id.error_layout);
        errorText = findViewById(R.id.error_text);

        serverAddress.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                serverNext();
                return true;
            }
            return false;
        });

        password.setEndIconOnClickListener(v -> passwordNext());
        password.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                passwordNext();
                return true;
            }
            return false;
        });

        serverAddress.requestFocus();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void serverNext() {
        setMessage(getString(R.string.server_check), false);

        model.checkServer(serverAddress.getEditText().getText().toString()).observe(this, success -> {
            serverCheckComplete(success ? 0 : 1);
        });
    }

    private void passwordNext() {
        setMessage(getString(R.string.credential_check), false);

        String serverAddress = this.serverAddress.getEditText().getText().toString();
        String username = this.username.getEditText().getText().toString();
        String password = this.password.getEditText().getText().toString();

        model.auth(serverAddress, username, password).observe(this, sessionId -> {
            userCheckComplete(sessionId != null ? 0 : 2, sessionId, serverAddress);
        });
    }

    private void serverCheckComplete(int status) {
        displayStatus(status);

        if (status == STATUS_OK) {
            setMessage(null, false);
            username.requestFocus();
        }
    }

    private void userCheckComplete(int status, String sessionId, String serverAddress) {
        displayStatus(status);

        if (status == STATUS_OK) {
            SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.preferences_server_address), serverAddress);
            editor.putString(getString(R.string.preferences_session_id), sessionId);
            editor.apply();
            finish();
        } else {
            username.requestFocus();
        }
    }

    private void displayStatus(int status) {
        switch (status) {
            case STATUS_NO_SERVER:
                setMessage(getString(R.string.server_error), true);
                break;
            case STATUS_WRONG_CREDENTIALS:
                setMessage(getString(R.string.wrong_credentials), true);
                break;
            default:
                setMessage(null, false);
                break;
        }
    }

    private void setMessage(String message, boolean error) {
        if (message == null) {
            infoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else if (error) {
            infoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText(message);
        } else {
            errorLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);
            infoText.setText(message);
        }
    }
}