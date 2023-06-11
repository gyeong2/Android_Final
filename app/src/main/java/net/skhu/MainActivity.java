package net.skhu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    RecyclerView1Adapter recyclerView1Adapter;
    ArrayList<String> arrayList;
    ArrayList<String> keyList = new ArrayList<>();
    DatabaseReference item02;

    ChildEventListener firebaseListener = new ChildEventListener() {
        private int findIndex(String key) {
            return Collections.binarySearch(keyList, key);
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();
            String item = dataSnapshot.getValue(String.class);
            arrayList.add(item);
            keyList.add(key);
            recyclerView1Adapter.notifyItemInserted(arrayList.size() - 1);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            String key = dataSnapshot.getKey();
            String item = dataSnapshot.getValue(String.class);
            int index = findIndex(key);
            arrayList.set(index, item);
            recyclerView1Adapter.notifyItemChanged(index);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            int index = findIndex(key);
            arrayList.remove(index);
            keyList.remove(index);
            recyclerView1Adapter.notifyItemRemoved(index);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<String>();

        recyclerView1Adapter = new RecyclerView1Adapter(this, arrayList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerView1Adapter);

        Button b = findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText e = findViewById(R.id.input1);
                String s = e.getText().toString();
                e.setText("");
                String key = item02.push().getKey();
                item02.child(key).setValue(s);
            }
        });
        this.item02 = FirebaseDatabase.getInstance().getReference("item03");
        this.item02.addChildEventListener(firebaseListener);
    }

    public void onMemoClicked(int itemIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int index) {
                item02.child(keyList.get(itemIndex)).removeValue();
            }
        });
        builder.setNegativeButton("아니오", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}





