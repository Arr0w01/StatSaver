package fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrowstatsaver.ListAdapter;
import com.example.arrowstatsaver.R;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;

import static com.example.arrowstatsaver.MainActivity.files;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class statusFragment extends Fragment {

    RecyclerView mRecyclerView, dummy ;

    GridLayoutManager gridLayoutManager;
    public static String version;
    RotateLoading rotateLoading;

    public statusFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        // Inflate the layout for this fragment

            dummy = view.findViewById(R.id.dummy);
            rotateLoading = view.findViewById(R.id.rotateloading);

            rotateLoading.start();

            mRecyclerView = (RecyclerView)view.findViewById(R.id.RvMain);
            gridLayoutManager = new GridLayoutManager(getContext(), 3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            gridLayoutManager.setReverseLayout(true);
//
//
            mRecyclerView.setLayoutManager(gridLayoutManager);



        File a = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/WhatsApp/Media/.Statuses");
        files = a.listFiles();
        ListAdapter rvMediaAdapter = new ListAdapter(getContext(), files);
//
           final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dummy.setVisibility(View.GONE);
//
            }
        },5000);
//
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateLoading.stop();
                rotateLoading.setVisibility(View.GONE);

            }
        },4500);

            mRecyclerView.setAdapter(rvMediaAdapter);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemViewCacheSize(rvMediaAdapter.getItemCount());

            mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
//               mRecyclerView.smoothScrollToPosition(rvMediaAdapter.getItemCount() -1);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
//
        });



        return view;
    }


}