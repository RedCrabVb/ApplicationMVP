package ru.vivt.applicationmvp.ui.accounts;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.vivt.applicationmvp.R;

public class Accounts extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getExtras().getString("action");
        switch (action) {
            case "accounts":
                setContentView(R.layout.fragment_autrization);
                findViewById(R.id.buttonAuthorization).setOnClickListener(this);
                break;
            case "registration":
                setContentView(R.layout.fragment_regestration);
                findViewById(R.id.buttonRegistration).setOnClickListener(this);
                break;
            default:
                setContentView(R.layout.activity_main);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAuthorization:
                //
                Toast.makeText(this, "click autrization", Toast.LENGTH_LONG).show();
                this.finish();
                break;
            case R.id.buttonRegistration:
                //
                Toast.makeText(this, "click registration", Toast.LENGTH_LONG).show();
                this.finish();
                break;
            default:
                Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
        }
    }
}
