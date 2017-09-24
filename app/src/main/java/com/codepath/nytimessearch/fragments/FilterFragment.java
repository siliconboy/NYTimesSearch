package com.codepath.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.activities.SearchActivity;
import com.codepath.nytimessearch.models.Filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class FilterFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.etDate)
    EditText mBeginDate;
    @BindView(R.id.spinner)
    Spinner mSort;
    @BindView(R.id.cbSports)
    CheckBox mSports;
    @BindView(R.id.cbArts)
    CheckBox mArts;
    @BindView(R.id.cbFashion)
    CheckBox mFashionStyle;
    @BindView(R.id.btnSave)
    Button btnSave;
    private Unbinder unbinder;
    private Filter mFilter;
    private DatePickerDialog dueDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilterFragment.
     */
    public static FilterFragment newInstance(Filter ft) {
        FilterFragment fragment = new FilterFragment();

        fragment.setFilter(ft);
        return fragment;
    }

    public void setFilter(Filter ft) {
        mFilter = ft;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container);
        unbinder = ButterKnife.bind(this, view);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        btnSave.setOnClickListener(view1 -> {

            mFilter.setArt(mArts.isChecked());

            Date dt = null;
            try {
                dt = dateFormatter.parse(mBeginDate.getText().toString());
            } catch (ParseException e) {
                //skip . we have this field filled using datepicker.
            }
            mFilter.setBeginDate(mBeginDate.getText().toString());
            mFilter.setFashionStyle(mFashionStyle.isChecked());
            mFilter.setSports(mSports.isChecked());

            mFilter.setSort(mSort.getSelectedItem().toString());
            SearchActivity act = (SearchActivity) getActivity();
            act.onFinishDialog(mFilter);
            dismiss();
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load dropdown list
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSort.setAdapter(adapter);

        if (mFilter == null) {
            mFilter = new Filter();
        } else {

            mBeginDate.setText(mFilter.getBeginDate());
            if (mFilter.getSort() != null) {
                int spinnerPosition = adapter.getPosition(mFilter.getSort());
                mSort.setSelection(spinnerPosition);
            }
        }

        mBeginDate.setOnClickListener(this);
        //solve two click issue for edittext field.
        mBeginDate.setFocusable(false);

        String pDate = mFilter.getBeginDate();
        SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String[] mdys=null;
        if(pDate!=null && !pDate.isEmpty()){
            mdys= pDate.split("/");
        }

        Calendar newCalendar = Calendar.getInstance();
        int year = pDate == null ? newCalendar.get(Calendar.YEAR) : Integer.valueOf(mdys[2]);
        int month = pDate == null ? newCalendar.get(Calendar.MONTH) : Integer.valueOf(mdys[0])-1;
        int dayofmonth = pDate == null ? newCalendar.get(Calendar.DAY_OF_MONTH) : Integer.valueOf(mdys[1]);

        dueDatePickerDialog = new DatePickerDialog(this.getContext(), (view1, year1, month1, dayOfMonth) -> {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year1, month1, dayOfMonth);
            mBeginDate.setText(dateFormatter.format(newDate.getTime()));
        }, year, month, dayofmonth);

        mArts.setChecked(mFilter.getArt());
        mFashionStyle.setChecked(mFilter.getFashionStyle());
        mSports.setChecked(mFilter.getSports());

    }

    @Override
    public void onClick(View view) {
        if (view == mBeginDate) {
            dueDatePickerDialog.show();
        }
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
