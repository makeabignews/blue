package com.gpiopi.www.bluelight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;


    private BluetoothAdapter ba;
    private BluetoothSocket bsocket = null;
    private OutputStream ostream = null;
    private InputStream istream = null;

    String light_address=""; //灯的地址
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//串口UUID

    public void SendStr(String str) {
        byte b[] = str.getBytes();
        if((!str.equals("")) && (bsocket!=null)) {
            try {
                ostream = bsocket.getOutputStream();
                ostream.write(b);
                //ostream.write('\0');    // Send an ending sign
            } catch (IOException e) {
                e.printStackTrace();
                //setTitle("err");
            }
            /*
            try {
                if (ostream != null) {
                    ostream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            // 设备不支持蓝牙功能
        }else{
            ba.enable();
            light_address="98:D3:34:91:11:66";
            BluetoothDevice device = ba.getRemoteDevice(light_address);
            try {
                bsocket = device.createRfcommSocketToServiceRecord(
                        uuid);
                //ba.cancelDiscovery();
                bsocket.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        setTitle("蓝牙智能电灯");

        final Switch btn_switch=(Switch)findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_switch.isChecked()){
                    SendStr("1");
                    setTitle("智能蓝牙灯（已开启）");
                    btn_switch.setText("点击关闭");
                }else{
                    SendStr("0");
                    setTitle("智能蓝牙灯（已关闭）");
                    btn_switch.setText("点击开启");
                }
            }
        });

    }
}
