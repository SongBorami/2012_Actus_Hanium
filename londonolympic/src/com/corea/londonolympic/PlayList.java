package com.corea.londonolympic;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.corea.londonolympic.sqlite.PlayListHepler;

public class PlayList extends Activity {
	
	protected ListAdapter adapter;
	ArrayList<HashMap<String, String>> list;
	ArrayList<HashMap<String, String>> Result;
	LayoutInflater inflater;
	
	TextView textok;
	
	private AlarmManager am;
	long timeset;
	Intent i;
	PendingIntent p;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	final CharSequence[] alrambtn = {"ON"};
    	final int colors[] = { 0xFFFF0000};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    
	    i = new Intent(getBaseContext(), AlarmReceiver.class);
	    p = PendingIntent.getBroadcast(getBaseContext(), 0, i, 0);
        
       
        
        
        Intent intent = getIntent(); 					//Schedule���� Intent�� �޾ƿ�
        String play = intent.getStringExtra("date"); 	// �޾ƿ� �����͸� paly�־��ش�.
        
        textok = (TextView)findViewById(R.id.textok);
        textok.setText(play); 							// ��Ƽ��Ƽ�� ��¥���� text
        
       
        //����Ʈ�信 ���� ����. 
		final ListView listView = (ListView) findViewById(R.id.list);
		inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = new ArrayList<HashMap<String,String>>();

		//DB����
		PlayListHepler db = new PlayListHepler(getApplicationContext());
		list = db.getAllPlays(play);
		Result = new ArrayList<HashMap<String,String>>(list);
        
		//��¥�� �°� ����Ʈ ����
		final CustomAdapter adapter = new CustomAdapter(this, R.layout.main, Result);
    	listView.setAdapter(adapter);

    	//����Ʈ�� Ŭ���ϸ� �˶� ���� ��ư ������.
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    		public void onItemClick(AdapterView<?> parent, final View view, int position,
    				long id) {
    			
    			new AlertDialog.Builder(PlayList.this)
				.setTitle("�˶� ����")
				.setSingleChoiceItems(alrambtn, -1, 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						
						am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, p);
						view.setBackgroundColor(0xFFFF0000);
						//listView.setBackgroundColor(0xFFFF0000);//����Ʈ ���� �ٲٱ� 
						//setSelector
						/*
						 * 
						 * 
						���⿡ ��� �߰��ϱ�!!
						*
						*
						*
						*/
					}
				})
				.setNegativeButton("Ȯ��", null)
				.show();			    	
    		} });

    }
    
    protected void startDialog() {
    	AlertDialog.Builder alt1 = new AlertDialog.Builder(this);  
        alt1.setMessage("����Ʈ�� �����ø� ���ð� �˶� ������ �����մϴ�.")
        .setCancelable(false)
        .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() { 
        	public void onClick(DialogInterface dialog, int id) {          	
        		} 
        	});  
        AlertDialog alt2 = alt1.create();        
        alt2.setTitle("����");                 
        alt2.show();
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		startDialog();
	}
    
    //DB������ Hashmap�� ����?
	private class CustomAdapter extends ArrayAdapter<HashMap<String, String>>{
		public CustomAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> Strings) {
			super(context, textViewResourceId, Strings);
		}
		private class ViewHolder{
			TextView info, explain, time, date;
			
		}
		ViewHolder viewHolder;
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){        
				convertView=inflater.inflate(R.layout.row, null);
				viewHolder=new ViewHolder();
				
				viewHolder.date=(TextView) convertView.findViewById(R.id.date);
				viewHolder.time=(TextView) convertView.findViewById(R.id.time);
				viewHolder.info=(TextView) convertView.findViewById(R.id.info);
				viewHolder.explain=(TextView) convertView.findViewById(R.id.explain);
				
				
				convertView.setTag(viewHolder);
				
			} else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.date.setText(Result.get(position).get("date"));
			viewHolder.time.setText(Result.get(position).get("time"));
			viewHolder.info.setText(Result.get(position).get("info"));
			viewHolder.explain.setText(Result.get(position).get("explain"));
			
		
			
			return convertView;
		}
	}
	
		
	
    
}