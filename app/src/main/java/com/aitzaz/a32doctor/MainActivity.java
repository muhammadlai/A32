package com.aitzaz.a32doctor;
import android.Manifest; import android.app.*; import android.content.*; import android.os.*; import android.widget.*;
public class MainActivity extends Activity {
 TextView status,health,metrics; boolean gaming=false;
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);status=findViewById(R.id.status);health=findViewById(R.id.health);metrics=findViewById(R.id.metrics);
 if(Build.VERSION.SDK_INT>=33)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},10);
 findViewById(R.id.start).setOnClickListener(v->startDoctor());
 findViewById(R.id.stop).setOnClickListener(v->{stopService(new Intent(this,DoctorService.class));status.setText("Doctor stopped");});
 findViewById(R.id.gaming).setOnClickListener(v->{gaming=!gaming;DoctorService.gaming=gaming;((Button)v).setText("SMART GAMING MODE: "+(gaming?"ON":"OFF"));});
 findViewById(R.id.touch).setOnClickListener(v->startActivity(new Intent(this,TouchTestActivity.class)));
 findViewById(R.id.scan).setOnClickListener(v->refresh());refresh();}
 void startDoctor(){if(Build.VERSION.SDK_INT>=26)startForegroundService(new Intent(this,DoctorService.class));else startService(new Intent(this,DoctorService.class));status.setText("Doctor running in background");}
 void refresh(){Intent i=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));if(i!=null){int l=i.getIntExtra("level",0),t=i.getIntExtra("temperature",0);float c=t/10f;metrics.setText("Battery: "+l+"%\nTemperature: "+c+" °C\nStorage: monitored\nRAM: monitored");health.setText("Health: "+(c>=47?"CRITICAL HEAT":c>=42?"HOT":"OK"));}}
}