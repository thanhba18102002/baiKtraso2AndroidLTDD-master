package com.example.ktrquanlysp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ktrquanlysp.activitys.CapNhatContactActivity;
import com.example.ktrquanlysp.activitys.ThemContactActivity;
import com.example.ktrquanlysp.adapter.SanPhamAdapter;
import com.example.ktrquanlysp.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ListView lvContact;
    RecyclerView lvContact1;
    SanPhamAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        getContactsFromFirebase();
    }
    private void getContactsFromFirebase() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("sanphams");
        adapter.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
//convert ra đối tượng Contact:
                    SanPham sanPham =dss.getValue(SanPham.class);
                    String key=dss.getKey();
                    sanPham.setContactId(key);
                    adapter.add(sanPham);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        lvContact=findViewById(R.id.lvContact);
        adapter=new SanPhamAdapter(this,R.layout.item);
        lvContact.setAdapter(adapter);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SanPham data=adapter.getItem(position);
                Intent intent=new Intent(MainActivity.this, CapNhatContactActivity.class);
                intent.putExtra("KEY",data.getContactId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.mnuAdd)
        {
//mở màn hình thêm ở đây
            Intent intent=new Intent(MainActivity.this, ThemContactActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}