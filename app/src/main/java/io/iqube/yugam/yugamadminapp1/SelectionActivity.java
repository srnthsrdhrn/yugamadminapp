package io.iqube.yugam.yugamadminapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import io.iqube.yugam.yugamadminapp1.models.ManagedEvent;
import io.iqube.yugam.yugamadminapp1.models.ManagedWorkshop;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class SelectionActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Realm.init(this);
        final Realm realm = Realm.getInstance(new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build());
        RealmResults<ManagedEvent> events = realm.where(ManagedEvent.class).findAll().sort("title");
        final RealmResults<ManagedWorkshop> workshops = realm.where(ManagedWorkshop.class).findAll().sort("title");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        for(int i=0;i<events.size();i++){
            adapter.add(events.get(i).getTitle());
        }
        for(int i=0;i<workshops.size();i++){
            adapter.add(workshops.get(i).getTitle());
        }
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                ManagedWorkshop workshops1 = realm.where(ManagedWorkshop.class).equalTo("title",item).findFirst();
                if(workshops1!=null){
                    startActivity(new Intent(SelectionActivity.this,AttendanceActivity.class)
                            .putExtra("particular",1)
                            .putExtra("id",workshops1.getId())
                    );
                }else{
                    ManagedEvent events1 = realm.where(ManagedEvent.class).equalTo("title",item).findFirst();
                    if(events1!=null){
                        startActivity(new Intent(SelectionActivity.this,AttendanceActivity.class)
                                .putExtra("particular",0)
                                .putExtra("id",events1.getId())
                        );
                    }else{
                        Toast.makeText(SelectionActivity.this, "Error, Try Restarting the App", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
