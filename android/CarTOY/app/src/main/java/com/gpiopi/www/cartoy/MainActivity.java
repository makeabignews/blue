package com.gpiopi.www.cartoy;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //System.out.println("handleMessage thread id " + Thread.currentThread().getId());
                    //System.out.println("msg.arg1:" + msg.arg1);
                    //System.out.println("msg.arg2:" + msg.arg2);
                    String inmsg=(String)msg.obj;


                    //((TextView)MainActivity.this.findViewById(R.id.button)).setText(inmsg);
                    break;
            }
        }
    };
    private BluetoothAdapter ba;
    private BluetoothSocket bsocket = null;
    private OutputStream ostream = null;
    private InputStream istream = null;
    private BluetoothGatt bluetoothGatt = null;
    BluetoothDevice device = null;
    String address="00:15:83:00:8D:EE"; //carToy的地址
    String UUID_KEY_SERVICE="0000ffe0-0000-1000-8000-00805f9b34fb";
    String UUID_KEY_WRITE="0000ffe1-0000-1000-8000-00805f9b34fb";
    UUID uuid = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void conn(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Message message = Message.obtain();
            message.obj = "该设备不支持蓝牙4.0!";
            message.what = 1;
            uiHandler.sendMessage(message);
            //finish();
        }

        ba = BluetoothAdapter.getDefaultAdapter();
        //ba.disable();
        //ba.enable();
        device = ba.getRemoteDevice(address);
        bluetoothGatt = device.connectGatt(this, true, gattCallback);

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_up = (Button) findViewById(R.id.button_up);
        Button button_down = (Button) findViewById(R.id.button_down);
        Button button_left = (Button) findViewById(R.id.button_left);
        Button button_right = (Button) findViewById(R.id.button_right);
        Button button_conn = (Button) findViewById(R.id.button_CONN);

        setTitle("蓝牙遥控车");



        conn();

        button_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conn();
            }
        });

        button_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    write(("5").getBytes());
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    write(("1").getBytes());
                }
                return false;
            }
        });

        button_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    write(("5").getBytes());
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    write(("2").getBytes());
                }
                return false;
            }
        });

        button_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    write(("5").getBytes());
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    write(("4").getBytes());
                }
                return false;
            }
        });

        button_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    write(("5").getBytes());
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    write(("3").getBytes());
                }
                return false;
            }
        });

    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status,int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                bluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
                Message message = Message.obtain();
                message.obj = "连接成功";
                message.what = 1;
                uiHandler.sendMessage(message);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                Message message = Message.obtain();
                message.obj = "连接失败";
                message.what = 1;
                uiHandler.sendMessage(message);
            }
        }
        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
                Message message = Message.obtain();
                message.obj = "服务正常";
                message.what = 1;
                uiHandler.sendMessage(message);
            } else {
                //Log.w(TAG, "onServicesDiscovered received: " + status);
                Message message = Message.obtain();
                message.obj = "没有找到服务";
                message.what = 1;
                uiHandler.sendMessage(message);
            }
        }
        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("onCharacteristicRead");
            Message message = Message.obtain();
            message.obj = "正在读取设备";
            message.what = 1;
            uiHandler.sendMessage(message);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override //当向设备Descriptor中写数据时，会回调该函数
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            System.out.println("onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue() != null) {
                System.out.println(characteristic.getStringValue(0));
                Message message = Message.obtain();
                message.obj = "设备通知";
                message.what = 1;
                uiHandler.sendMessage(message);
            }
            System.out.println("--------onCharacteristicChanged-----");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            System.out.println("rssi = " + rssi);
        }
        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            Message message = Message.obtain();
            message.obj = "正在发送命令";
            message.what = 1;
            uiHandler.sendMessage(message);
            System.out.println("--------write success----- status:" + status);
        };
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public BluetoothGattService getService(UUID uuid) {
        Message message = Message.obtain();
        message.obj = "正在获得服务uuid";
        message.what = 1;
        uiHandler.sendMessage(message);
        return bluetoothGatt.getService(uuid);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothGattCharacteristic getCharcteristic(String serviceUUID, String characteristicUUID) {

//得到服务对象
        BluetoothGattService service = getService(UUID.fromString(serviceUUID));  //调用上面获取服务的方法

        if (service == null) {
            //TLog.e(TAG, "Can not find 'BluetoothGattService'");
            Message message = Message.obtain();
            message.obj = "Can not find BluetoothGattService";
            message.what = 1;
            uiHandler.sendMessage(message);
            return null;
        }

//得到此服务结点下Characteristic对象
        final BluetoothGattCharacteristic gattCharacteristic = service.getCharacteristic(UUID.fromString(characteristicUUID));
        if (gattCharacteristic != null) {
            return gattCharacteristic;
        } else {
            //TLog.e(TAG, "Can not find 'BluetoothGattCharacteristic'");
            Message message = Message.obtain();
            message.obj = "Can not find BluetoothGattCharacteristic";
            message.what = 1;
            uiHandler.sendMessage(message);
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void write(byte[] data) {   //一般都是传byte
        BluetoothGattCharacteristic writeCharacteristic = getCharcteristic(UUID_KEY_SERVICE, UUID_KEY_WRITE);  //这个UUID都是根据协议号的UUID
        if (writeCharacteristic == null) {
            //TLog.e(TAG, "Write failed. GattCharacteristic is null.");
            Message message = Message.obtain();
            message.obj = "Write failed. GattCharacteristic is null.";
            message.what = 1;
            uiHandler.sendMessage(message);
            return;
        }
        writeCharacteristic.setValue(data); //为characteristic赋值
        writeCharacteristicWrite(writeCharacteristic);

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void writeCharacteristicWrite(BluetoothGattCharacteristic characteristic) {
        if (ba == null || bluetoothGatt == null) {
            //TLog.e(TAG, "BluetoothAdapter not initialized");
            Message message = Message.obtain();
            message.obj = "BluetoothAdapter not initialized";
            message.what = 1;
            uiHandler.sendMessage(message);
            return;
        }
        //TLog.e(TAG, "BluetoothAdapter 写入数据");
        boolean isBoolean = false;
        isBoolean = bluetoothGatt.writeCharacteristic(characteristic);
        Message message = Message.obtain();
        if(isBoolean){
            message.obj = "^_^";
        }else{
            message.obj = ">_<";
        }

        message.what = 1;
        uiHandler.sendMessage(message);
        //TLog.e(TAG, "BluetoothAdapter_writeCharacteristic = " +isBoolean);  //如果isBoolean返回的是true则写入成功
    }

    //获取数据
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (ba == null || bluetoothGatt == null) {
            //TLog.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (ba == null || bluetoothGatt == null) {
            //TLog.e(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        return bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }
}
