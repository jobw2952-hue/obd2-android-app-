ckage com.niot.obdscanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class NiotObdManager {
    // NIOT OBD Link UUID (Standard SPP)
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private Context context;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isConnected = false;
    
    public NiotObdManager(Context context) {
        this.context = context;
    }
    
    // Connect to NIOT OBD Link adapter
    public boolean connectToNiotObd() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                showToast("Bluetooth not supported");
                return false;
            }
            
            if (!adapter.isEnabled()) {
                showToast("Please enable Bluetooth");
                return false;
            }
            
            // Find NIOT OBD device
            for (BluetoothDevice device : adapter.getBondedDevices()) {
                String deviceName = device.getName();
                if (deviceName != null && 
                    (deviceName.contains("NIOT") || 
                     deviceName.contains("OBD") || 
                     deviceName.contains("ELM327"))) {
                    
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    socket.connect();
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    isConnected = true;
                    
                    // Initialize OBD adapter
                    initializeOBD();
                    
                    showToast("Connected to " + deviceName);
                    return true;
                }
            }
            
            showToast("NIOT OBD Link not found. Pair it in Bluetooth settings.");
            
        } catch (Exception e) {
            showToast("Connection error: " + e.getMessage());
        }
        return false;
    }
    
    private void initializeOBD() {
        try {
            // Reset OBD adapter
            sendCommand("ATZ");
            Thread.sleep(1000);
            
            // Echo off
            sendCommand("ATE0");
            
            // For VW Polo 2013 (CAN protocol)
            sendCommand("ATSP6");  // ISO 15765-4 CAN
            
            showToast("OBD initialized for VW Polo");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Read trouble codes (like P0036)
    public String readTroubleCodes() {
        if (!isConnected) return "Not connected";
        
        try {
            sendCommand("03");  // Mode 03: Read DTCs
            Thread.sleep(1000);
            
            byte[] buffer = new byte[1024];
            int bytes = inputStream.read(buffer);
            String response = new String(buffer, 0, bytes);
            
            return parseDTCResponse(response);
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // Clear trouble codes (like P0036)
    public boolean clearTroubleCodes() {
        if (!isConnected) return false;
        
        try {
            sendCommand("04");  // Mode 04: Clear DTCs
            Thread.sleep(1000);
            
            showToast("Trouble codes cleared");
            return true;
            
        } catch (Exception e) {
            showToast("Clear failed: " + e.getMessage());
            return false;
        }
    }
    
    // Read live data (RPM, speed, etc.)
    public String readLiveData(String pid) {
        if (!isConnected) return "0";
        
        try {
            sendCommand(pid);
            Thread.sleep(300);
            
            byte[] buffer = new byte[1024];
            int bytes = inputStream.read(buffer);
            String response = new String(buffer, 0, bytes);
            
            return parsePIDResponse(pid, response);
            
        } catch (Exception e) {
            return "Error";
        }
    }
    
    // Parse OBD2 response
    private String parseDTCResponse(String response) {
        // Clean response
        String clean = response.replaceAll("[^0-9A-F]", "");
        
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < clean.length(); i += 4) {
            if (i + 4 <= clean.length()) {
                String hex = clean.substring(i, i + 4);
                if (!hex.equals("0000")) {
                    String dtc = hexToDTC(hex);
                    codes.append(dtc).append("\n");
                }
            }
        }
        
        return codes.length() > 0 ? codes.toString() : "No trouble codes found";
    }
    
    private String hexToDTC(String hex) {
        if (hex.length() != 4) return "Invalid";
        
        char first = hex.charAt(0);
        String prefix = switch (first) {
            case '0', '1' -> "P";
            case '2', '3' -> "C";
            case '4', '5' -> "B";
            case '6', '7' -> "U";
            default -> "P";
        };
        
        return prefix + hex.substring(1);
    }
    
    private String parsePIDResponse(String pid, String response) {
        // Simple parsing
        String clean = response.replaceAll("[^0-9A-F]", "");
        
        if (clean.length() >= 4) {
            String data = clean.substring(clean.length() - 4);
            
            switch (pid) {
                case "010C":  // RPM
                    int rpm = Integer.parseInt(data, 16) / 4;
                    return rpm + " RPM";
                    
                case "010D":  // Speed
                    int speed = Integer.parseInt(data.substring(0, 2), 16);
                    return speed + " km/h";
                    
                default:
                    return data;
            }
        }
        return "No data";
    }
    
    // Helper methods
    private void sendCommand(String command) {
        try {
            if (outputStream != null) {
                outputStream.write((command + "\r").getBytes());
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showToast(String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
    
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            isConnected = false;
            showToast("Disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
