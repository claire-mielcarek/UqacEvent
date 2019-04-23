package com.example.clair.uqacevent.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.clair.uqacevent.MainActivity;
import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterFragment extends Fragment {
    private DatabaseReference data;
    private FirebaseAuth auth;
    private FirebaseUser user;

    ArrayAdapter<String> listAdapter;
    ArrayList<String> organizers;
    ArrayList<String> organizersIds;
    ValueEventListener organizersListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getTag());
        return inflater.inflate(R.layout.dashboard_filter, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference();

        organizers = new ArrayList<>();
        organizersIds = new ArrayList<>();
        organizers.add("Coucou");
        organizers.add("Personne qui fait des soirées soirées  soirées soirées soirées");
        addOrganizersListener();
        ListView list = getActivity().findViewById(R.id.filter_list);
        listAdapter = new ArrayAdapter<String>(getContext(), R.layout.dashboard_filter_item, R.id.filter_list_text, organizers);
        list.setAdapter(listAdapter);
    }

    private void addOrganizersListener() {

        organizersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("[GROUP_LISTENER]", "data change");
                DataSnapshot groupsData = dataSnapshot.child("Users");
                boolean isUserGroup = false;
                for (DataSnapshot user : groupsData.getChildren()) {
                    if (!organizersIds.contains(user.getKey()) && user.child("accountIsPublic").getValue().equals("true")) {
                        organizersIds.add(user.getKey());
                        organizers.add((String) user.child("nom").getValue());
                    }
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("[ FILTER ]", "Failed to read value.", error.toException());
            }
        };
        data.addValueEventListener(this.organizersListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_filter:
                saveFilters();
                Fragment f = new DashboardFragment();
                ((MainActivity) getActivity()).openFragment(f, getString(R.string.title_dashboard));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveFilters() {
    }
}
