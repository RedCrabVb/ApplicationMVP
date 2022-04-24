package ru.vivt.applicationmvp;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import ru.vivt.applicationmvp.databinding.ActivityMainBinding;
import ru.vivt.applicationmvp.databinding.ActivityStartBinding;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class StartActivity extends AppCompatActivity {
    private static final String serverIP = "servermvp.ru:49207";
//    private static final String serverIP = "10.0.2.2:8080";

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.enterIP.setOnClickListener(l -> {
            connection(binding.editTextIp.getText().toString());
        });

        connection(serverIP);
    }

    private void connection(String ip) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.inputIP.setVisibility(GONE);

        Server server = Server.getInstance(ip);
        MemoryValues memoryValues = MemoryValues.init(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String tokenInMemory = memoryValues.getToken();
        if (tokenInMemory != null) {
            server.setTokenConnection(tokenInMemory);
        } else {
            requestQueue.add(server.registration2(response -> server.saveDataInMemory(memoryValues, response)));
        }

        server.saveDataInMemory(memoryValues);

        requestQueue.add(server.getNewsRequest(response -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }, error -> {
            binding.inputIP.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(GONE);
        }));
    }


}