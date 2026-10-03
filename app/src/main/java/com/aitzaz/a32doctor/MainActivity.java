package com.aitzaz.a32doctor;
import android.Manifest; import android.app.*; import android.content.*; import android.os.*; import android.app.ActivityManager; import android.widget.*;
public class MainActivity extends Activity {
 TextView status,health,metrics,batteryValue,chargingValue,tempValue,tempState; boolean gaming=false;
 @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);
  status=findViewById(R.id.status);health=findViewById(R.id.health);metrics=findViewById(R.id.metrics);batteryValue=findViewById(R.id.batteryValue);chargingValue=findViewById(R.id.chargingValue);tempValue=findViewById(R.id.tempValue);tempState=findViewById(R.id.tempState);
  if(Build.VERSION.SDK_INT>=33)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},10);
  findViewById(R.id.start).setOnClickListener(v->startDoctor());
  findViewById(R.id.stop).setOnClickListener(v->{stopService(new Intent(this,DoctorService.class));status.setText("Doctor is stopped");});
  findViewById(R.id.gaming).setOnClickListener(v->{gaming=!gaming;DoctorService.gaming=gaming;((Button)v).setText("SMART GAMING MODE: "+(gaming?"ON":"OFF"));status.setText(gaming?"Gaming protection is active":"Doctor is running normally");});
  findViewById(R.id.touch).setOnClickListener(v->startActivity(new Intent(this,TouchTestActivity.class)));
  findViewById(R.id.scan).setOnClickListener(v->refresh());
  refresh();
 }
 void startDoctor(){if(Build.VERSION.SDK_INT>=26)startForegroundService(new Intent(this,DoctorService.class));else startService(new Intent(this,DoctorService.class));status.setText("Doctor is monitoring your phone");refresh();}
 void refresh(){Intent i=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));if(i==null)return;
  int l=i.getIntExtra("level",0),t=i.getIntExtra("temperature",0);float c=t/10f;int scale=i.getIntExtra("scale",100);l=(int)(l*100f/Math.max(scale,1));
  int st=i.getIntExtra("status",-1);boolean charging=st==2||st==5;
  batteryValue.setText(l+"%");chargingValue.setText(charging?"Charging":"Not charging");tempValue.setText(String.format(java.util.Locale.US,"%.1f °C",c));
  tempState.setText(c>=47?"Critical heat":c>=42?"Hot":c>=38?"Warm":"Normal");
  ActivityManager am=(ActivityManager)getSystemService(ACTIVITY_SERVICE);ActivityManager.MemoryInfo mi=new ActivityManager.MemoryInfo();am.getMemoryInfo(mi);
  long total=mi.totalMem/1048576L, avail=mi.availMem/1048576L, used=total-avail;
  android.os.StatFs sf=new android.os.StatFs(android.os.Environment.getDataDirectory().getPath());long totalS=sf.getTotalBytes()/1073741824L, freeS=sf.getAvailableBytes()/1073741824L, usedS=totalS-freeS;
  metrics.setText("RAM: "+used+" MB used / "+total+" MB\nStorage: "+usedS+" GB used / "+totalS+" GB\nCharging: "+(charging?"Yes":"No")+"\nBattery: "+(l<=10?"Low":"Normal"));
  health.setText(c>=47?"CRITICAL":c>=42?"HOT":l<=10?"ATTENTION":"HEALTHY");
 }
 @Override protected void onResume(){super.onResume();refresh();}
}