package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arrowstatsaver.ListAdapter;
import com.example.arrowstatsaver.R;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static com.example.arrowstatsaver.MainActivity.savedFiles;


public class savedFragment extends Fragment {
    RecyclerView rv;
    GridLayoutManager gridLayoutManager;
    SwipeRefreshLayout refreshLayout;

    public savedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_saved, container, false);
        rv = view.findViewById(R.id.saveRV);
        refreshLayout = view.findViewById(R.id.swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRV();
                refreshLayout.setRefreshing(false);

             }

        });

         gridLayoutManager = new GridLayoutManager(getContext(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(gridLayoutManager);
        ListAdapter listAdapter = new ListAdapter(getContext(), savedFiles);
        rv.setAdapter(listAdapter);

        return view;
    }

    private void loadRV() {
        ListAdapter listAdapter = new ListAdapter(getContext(), savedFiles);
        rv.setAdapter(listAdapter);
    }
}