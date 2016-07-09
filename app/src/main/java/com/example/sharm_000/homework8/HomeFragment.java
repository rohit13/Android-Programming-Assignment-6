package com.example.sharm_000.homework8;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAboutMeClickedListener bListener;
    private OnTask2ClickedListener t2Listener;
    private OnTask3ClickedListener t3Listener;
    Button aboutMe;
    Button task2;
    Button task3;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance  () {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try{
            bListener = (OnAboutMeClickedListener) getContext();
            t2Listener= (OnTask2ClickedListener) getContext();
            t3Listener= (OnTask3ClickedListener) getContext();
        }catch(ClassCastException ex){
            throw new ClassCastException("forget to implement interface");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        aboutMe = (Button) view.findViewById(R.id.aboutme);
        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bListener.onAboutMeClicked(savedInstanceState);
            }
        });

        task2 = (Button) view.findViewById(R.id.task2);
        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2Listener.onTask2Clicked(savedInstanceState);
            }
        });

        task3 = (Button) view.findViewById(R.id.task3);
        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t3Listener.onTask3Clicked(savedInstanceState);
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bListener = null;
        t2Listener = null;
        t3Listener = null;
    }

    public interface OnAboutMeClickedListener{
        public void onAboutMeClicked(Bundle savedInstanceState);
    }

    public interface OnTask2ClickedListener{
        public void onTask2Clicked(Bundle savedInstanceState);
    }

    public interface OnTask3ClickedListener{
        public void onTask3Clicked(Bundle savedInstanceState);
    }
}
