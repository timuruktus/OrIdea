package ru.timuruktus.oridea.View.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ru.timuruktus.oridea.Events.ToMainActivity.ChangeToolbarTitleEvent;
import ru.timuruktus.oridea.Model.JSONFragments.Post;
import ru.timuruktus.oridea.R;
import ru.timuruktus.oridea.View.Activities.MainActivity;
import ru.timuruktus.oridea.View.Other.Adapters.UserPostsAdapter;


public class WelcomeFragment extends Fragment {

    public static final String TAG = "tag";

    UserPostsAdapter userPostsAdapter;
    private ArrayList<Post> postArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().post(new ChangeToolbarTitleEvent(R.string.title_activity_main));
        View rootView =
                inflater.inflate(R.layout.welcome_fragment, container, false);

        //userPostsAdapter = new UserPostsAdapter(rootView.getContext(), );

        ListView userPosts = (ListView) rootView.findViewById(R.id.userPosts);
        // TODO: Add posts to Adapter
        //userPosts.setAdapter(userPostsAdapter);
        userPosts.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        //EventBus.getDefault().register(this);
        super.onStart();

    }


}
