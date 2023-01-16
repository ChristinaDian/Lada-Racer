package com.example.simpleracer.ui.home;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.simpleracer.GameView;

public class HomeFragment extends Fragment {

   // private HomeViewModel homeViewModel;
    //private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Display display =getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        GameView view = new GameView(getActivity(), size.x,size.y);

        return view;
    }
}