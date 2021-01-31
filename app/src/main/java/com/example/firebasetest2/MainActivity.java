package com.example.firebasetest2;  // поменять название пакета на свой

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //поле ввода нового сообщения
    private EditText messageContent;
    private RecyclerView messagesRecyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //привязка по id
        messagesRecyclerView = findViewById(R.id.messageList);
        messageContent = findViewById(R.id.editTextMessage);
        mAuth = FirebaseAuth.getInstance();

        //создание адаптера и подключение его к recycleView
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //добавление itemTouchHelper для удаления элемента списка свайпом вправо
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Message mess = messageList.get(viewHolder.getAdapterPosition());
                db.collection("Название коллекции").document(mess.getContent() + " " +mess.getDate()).delete();
                messageList.remove(viewHolder.getAdapterPosition());
                messageAdapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(messagesRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();


        db.collection("Название коллекции").orderBy("date").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //измененный список всех документов в коллекции
                List<DocumentSnapshot> documents = value.getDocuments();

                messageList.clear();
                for (DocumentSnapshot doc : documents) {
                    //получаем данные из документа
                    String str = doc.getString("text");
                    String dat = doc.getString("date");
//                    String g = "";
//                    for(int i = 0;i<currentUser.getEmail().length();i++){
//                        if(currentUser.getEmail().charAt(i)!='@'){
//                            g=g+currentUser.getEmail().charAt(i);
//                        }
//                        else break;
//                    }
                    User user = new User(doc.getString("user"));
                    Message m = new Message(str,dat,user);
                    messageList.add(m);
                }
               // Collections.sort(messageList);
                messageAdapter.notifyDataSetChanged();
            }
        } );
    }




    //onClick
    public void sendMessage(View view) {
        String g = "";
        for(int i = 0;i<currentUser.getEmail().length();i++){
            if(currentUser.getEmail().charAt(i)!='@'){
                g=g+currentUser.getEmail().charAt(i);
            }
            else break;
        }
        User user = new User(g);
        Message mess = new Message(messageContent.getText().toString(),user);
        //HashMap
        Map<String, Object> data = new HashMap<>();
        String s = messageContent.getText().toString();
        String d = mess.getDate();
        data.put("text",s);
        data.put("date",d);
        data.put("user", g);
        //отправление стрингов в firebase
        db.collection("Название коллекции").document(mess.getContent()+ " " +mess.getDate()).set(data);
        messageContent.setText("");
        //messageList.add(mess);
        messageAdapter.notifyDataSetChanged();
    }

    public void reverse(View view) {
        //Collections.reverse(messageList);
//        Comparator<Message> comparator = new Comparator<Message>() {
//            @Override
//            public int compare(Message o1, Message o2) {
//                return o1.getDate().compareTo(o2.getDate());
//            }
//        };
    }
}