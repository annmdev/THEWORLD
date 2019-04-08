package com.example.r.theworld.presentation.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.adapter.FavAdapter;
import com.example.r.theworld.presentation.common.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesListFragment extends BaseFragment {

    public static FavoritesListFragment newInstance(){
        return new FavoritesListFragment();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.frgment_favorites;
    }

    @Override
    protected void onBindView(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.rv);

        final FavoritesDatabase favoritesDatabase = FavoritesDatabase.create(getContext());
        List<String> list = favoritesDatabase.getAll();
        Collections.reverse(list);

        final FavAdapter adapter = new FavAdapter(new FavAdapter.CheckedListener() {
            @Override
            public void onChecked(String place) {
                favoritesDatabase.delete(place);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter.addList(list);

    }
}