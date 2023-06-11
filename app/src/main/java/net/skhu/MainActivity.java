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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    RecyclerView1Adapter recyclerView1Adapter;
    ArrayList<String> arrayList;
    DatabaseReference item02;

    ValueEventListener firebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<ArrayList<String>> typeIndicator;
            typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
            ArrayList<String> temp  = dataSnapshot.getValue(typeIndicator);
            if (temp != null) {
                arrayList.clear();
                arrayList.addAll(temp);
                recyclerView1Adapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onCancelled(DatabaseError error) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<String>();
        arrayList.add("one");
        arrayList.add("two");

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
                arrayList.add(s);
                item02.setValue(arrayList);
                recyclerView1Adapter.notifyDataSetChanged();
            }
        });
        this.item02 = FirebaseDatabase.getInstance().getReference("item03");
        this.item02.addValueEventListener(firebaseListener);
    }

    public void onMemoClicked(int itemIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인");
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int index) {
                arrayList.remove(itemIndex);
                recyclerView1Adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("아니오", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}





