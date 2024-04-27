package com.example.clearmind;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link back_to_login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class back_to_login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public back_to_login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment back_to_login.
     */
    // TODO: Rename and change types and number of parameters
    public static back_to_login newInstance(String param1, String param2) {
        back_to_login fragment = new back_to_login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w("MainActivity", "frag onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w("MainActivity", "frag onCreateView");
        View view = inflater.inflate(R.layout.fragment_back_to_login,
                container, false);
        Button button = (Button) view.findViewById(R.id.back_to_login);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                Log.w("fragment", "fragment back to login");
                Toast t = Toast.makeText(getActivity(), "This is a test fragment to show how to implement fragment", Toast.LENGTH_LONG);
                t.show();
            }
        });
        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_back_to_login, container, false);
    }


}