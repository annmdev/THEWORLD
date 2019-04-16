package com.example.r.theworld.presentation.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.r.theworld.R;
import com.example.r.theworld.presentation.adapter.FavAdapter;
import com.example.r.theworld.presentation.common.BaseFragment;

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
        final RecyclerView recyclerView = view.findViewById(R.id.rv);
        final TextView tvForEmptyList = view.findViewById(R.id.empty_list_text);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Favorites");

        final FavoritesDatabase favoritesDatabase = FavoritesDatabase.create(getContext());
        List<String> list = favoritesDatabase.getAll();
        Collections.reverse(list);

        if (list.size() != 0) {

            recyclerView.setVisibility(View.VISIBLE);
            tvForEmptyList.setVisibility(View.GONE);

            final FavAdapter adapter = new FavAdapter(new FavAdapter.Listener() {
                @Override
                public void onChecked(String place) {
                    favoritesDatabase.delete(place);
                }

                @Override
                public void onEmptyList() {
                    tvForEmptyList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));

            adapter.addList(list);

        }

    }
}