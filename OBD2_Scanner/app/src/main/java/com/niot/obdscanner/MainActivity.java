ckage com.niot.obdscanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    private TextView tvStatus;
    private Button btnConnect, btnScan, btnClear;
    private NiotObdManager obdManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize UI
        tvStatus = findViewById(R.id.tvStatus);
        btnConnect = findViewById(R.id.btnConnect);
        btnScan = findViewById(R.id.btnScan);
        btnClear = findViewById(R.id.btnClear);
        
        obdManager = new NiotObdManager(this);
        
        // Set button click listeners
        btnConnect.setOnClickListener(v -> connectToObd());
        btnScan.setOnClickListener(v -> scanTroubleCodes());
        btnClear.setOnClickListener(v -> clearTroubleCodes());
        
        tvStatus.setText("Ready to connect to NIOT OBD Link");
    }
    
    private void connectToObd() {
        tvStatus.setText("Connecting to NIOT OBD Link...");
        new Thread(() -> {
            boolean connected = obdManager.connectToNiotObd();
            runOnUiThread(() -> {
                if (connected) {
                    tvStatus.setText("Connected to VW Polo via CAN");
                    btnScan.setEnabled(true);
                    btnClear.setEnabled(true);
                } else {
                    tvStatus.setText("Connection failed. Check Bluetooth pairing.");
                }
            });
        }).start();
    }
    
    private void scanTroubleCodes() {
        tvStatus.setText("Scanning for trouble codes...");
        new Thread(() -> {
            String codes = obdManager.readTroubleCodes();
            runOnUiThread(() -> {
                tvStatus.setText("Scan Results:\n" + codes);
                
                // Check for P0036 specifically
                if (codes.contains("P0036")) {
                    tvStatus.append("\n\n P0036 Detected!\nFix: Check O2 sensor heater circuit\nCheck fuse #42 (10A)");
                }
            });
        }).start();
    }
    
    private void clearTroubleCodes() {
        tvStatus.setText("Clearing trouble codes...");
        new Thread(() -> {
            boolean cleared = obdManager.clearTroubleCodes();
            runOnUiThread(() -> {
                if (cleared) {
                    tvStatus.setText("Codes cleared successfully!");
                } else {
                    tvStatus.setText("Failed to clear codes");
                }
            });
        }).start();
    }
}
