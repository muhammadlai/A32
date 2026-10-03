package com.aitzaz.a32doctor;

import android.app.*; import android.content.*; import android.os.*; import android.content.Intent; import android.app.NotificationManager;

public class DoctorService extends Service {
 public static boolean gaming=false; Handler h=new Handler(); int lastTemp=-1;
 public int onStartCommand(Intent i,int f,int id){create(); h.removeCallbacksAndMessages(null); h.post(loop); return START_STICKY;}
 void create(){String ch="doctor"; NotificationManager nm=getSystemService(NotificationManager.class); if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(new NotificationChannel(ch,"Phone Doctor",NotificationManager.IMPORTANCE_LOW)); startForeground(7,new Notification.Builder(this,ch).setContentTitle("A32 Phone Doctor").setContentText("Monitoring phone health").setSmallIcon(android.R.drawable.ic_dialog_info).build());}
 Runnable loop=new Runnable(){public void run(){Intent b=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED)); if(b!=null){int temp=b.getIntExtra("temperature",0); float c=temp/10f; if(c>=47) alert("Critical temperature",String.format("Phone temperature is %.1f°C",c)); else if(c>=42) alert("Phone is hot",String.format("Temperature is %.1f°C",c)); int level=b.getIntExtra("level",100); if(level<=10) alert("Low battery","Battery is at "+level+"%");} h.postDelayed(this,gaming?15000:30000);}};
 void alert(String title,String msg){NotificationManager nm=getSystemService(NotificationManager.class); if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(new NotificationChannel("alerts","Health Alerts",NotificationManager.IMPORTANCE_HIGH)); nm.notify((int)(System.currentTimeMillis()%100000),new Notification.Builder(this,"alerts").setContentTitle(title).setContentText(msg).setSmallIcon(android.R.drawable.ic_dialog_alert).setAutoCancel(true).build());}
 public android.os.IBinder onBind(Intent i){return null;} public void onDestroy(){h.removeCallbacksAndMessages(null);super.onDestroy();}
}
