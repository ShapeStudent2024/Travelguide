package com.example.travelguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private AttractionAdapter adapter;
    private List<Attraction> attractionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadAttractionData();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setupRecyclerView() {
        attractionList = new ArrayList<>();
        adapter = new AttractionAdapter(attractionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadAttractionData() {
        attractionList.add(new Attraction(R.drawable.hong_kong_disneyland, "香港迪士尼樂園"));
        attractionList.add(new Attraction(R.drawable.ocean_park, "香港海洋公園"));
        attractionList.add(new Attraction(R.drawable.victoria_peak, "太平山頂"));

        adapter.notifyDataSetChanged();
    }
}