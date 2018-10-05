package com.example.android.worldheadlines;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.worldheadlines.adapter.CountriesAdapter;

public class LenguageFragment extends DialogFragment {

    public LenguageFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lenguage, container, false);

        String[] countriesCodes = this.getResources().getStringArray(R.array.country_code);
        String[] countryNames = this.getResources().getStringArray(R.array.country_names);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_fragment);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        CountriesAdapter mCountriesAdapter = new CountriesAdapter(countriesCodes, countryNames, getActivity(), getActivity());
        mRecyclerView.setAdapter(mCountriesAdapter);

        return rootView;
    }
}
