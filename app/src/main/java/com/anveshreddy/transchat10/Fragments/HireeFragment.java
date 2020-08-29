package com.anveshreddy.transchat10.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anveshreddy.transchat10.Adapters.HireAdapter;
import com.anveshreddy.transchat10.Adapters.HireeAdapter;
import com.anveshreddy.transchat10.Model.HireStatus;
import com.anveshreddy.transchat10.Model.HiringRequest;
import com.anveshreddy.transchat10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HireeFragment extends Fragment {
    private RecyclerView recyclerView1;
    private HireeAdapter HireeAdapter;
    private List<HireStatus> mUser;

    TextView tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_hiree, container, false);

        recyclerView1 = view.findViewById(R.id.hiree_recyclerView);
        recyclerView1.setHasFixedSize(true);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        mUser=new ArrayList<>();

        tv=view.findViewById(R.id.test);
        HireeAdapter=new HireeAdapter(getContext(),mUser,true);
        recyclerView1.setAdapter(HireeAdapter);
        readuser();
        return view;
    }
   private void readuser(){
       final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Hirestatus").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HireStatus hs = snapshot.getValue(HireStatus.class);

                    mUser.add(hs);



                }
                HireeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   }
}