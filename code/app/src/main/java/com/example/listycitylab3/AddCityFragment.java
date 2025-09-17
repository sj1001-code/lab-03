package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
//?
import java.io.Serializable;

public class AddCityFragment extends DialogFragment {

    private static final String ARG_CITY = "arg_city";
    private static final String ARG_POSITION = "arg_position";

    private City city;
    private int position = -1; // position of the city in the list

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, int position);
    }

    private AddCityDialogListener listener;

    public AddCityFragment() {}

    // create fragment for editing
    public static AddCityFragment newInstance(City city, int position) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (City) getArguments().getSerializable(ARG_CITY);
            position = getArguments().getInt(ARG_POSITION, -1);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        //..
        if (city != null) {
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());
        }
//.. ed
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(city == null ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(city == null ? "Add" : "Save", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (city != null) {
                        city.setName(cityName);
                        city.setProvince(provinceName);
                        listener.editCity(city, position); // pass position
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}

