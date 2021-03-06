package com.example.clair.uqacevent.Controller.Dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.clair.uqacevent.Controller.MainActivity;
import com.example.clair.uqacevent.Model.User;
import com.example.clair.uqacevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FilterFragment extends Fragment {
    private DatabaseReference data;
    Activity activity;

    FilterAdapter listAdapter;
    ArrayList<String> organizers;
    ArrayList<String> organizersIds;
    ArrayList<Boolean> filters;
    ValueEventListener organizersListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = Objects.requireNonNull(getActivity());
        activity.setTitle(getTag());
        return inflater.inflate(R.layout.dashboard_filter, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        
        setHasOptionsMenu(true);

        data = FirebaseDatabase.getInstance().getReference();

        organizers = new ArrayList<>();
        organizersIds = new ArrayList<>();
        filters = new ArrayList<>();
        addOrganizersListener();
        ListView list = activity.findViewById(R.id.filter_list);
        listAdapter = new FilterAdapter(getContext(), organizers, filters);
        list.setAdapter(listAdapter);
    }

    private void addOrganizersListener() {

        organizersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot groupsData = dataSnapshot.child("Users");
                ArrayList<String> alreadyFiltred = new ArrayList<>();
                if (User.getCurrentUser() != null) {
                    alreadyFiltred = User.getCurrentUser().getFilteredOrganizersIds();
                }
                Log.d("[FILTER]", "already filtred : " + alreadyFiltred );
                for (DataSnapshot user : groupsData.getChildren()) {
                    if (!organizersIds.contains(user.getKey()) && Objects.equals(user.child("accountIsPublic").getValue(), "true")) {
                        String organizerId = user.getKey();
                        organizersIds.add(organizerId);
                        organizers.add((String) user.child("nom").getValue());
                        filters.add(alreadyFiltred.contains(organizerId));
                    }
                }
                Log.d("[FILTER]", "etat initial des checkboxes : " + filters);
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
            case R.id.action_save:
                saveFilters();
                Fragment f = new DashboardFragment();
                ((MainActivity) activity).openFragment(f, getString(R.string.title_dashboard));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveFilters() {
        ArrayList<String> filteredOrganizersIds = new ArrayList<>();
        for (int i =0 ; i< organizersIds.size(); i++){
            if (filters.get(i)){
                filteredOrganizersIds.add(organizersIds.get(i));
            }
        }
        if (User.getCurrentUser() != null) {
            User.getCurrentUser().setFilteredOrganizersIds(filteredOrganizersIds);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
