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
import android.widget.DatePicker;
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
    private Unbinder unbinder;
//    private EditText mName;
    @BindView(R.id.etDate) EditText mBeginDate;
    @BindView(R.id.spinner) Spinner mSort;
    @BindView(R.id.cbSports) CheckBox mSports;
    @BindView(R.id.cbArts) CheckBox mArts;
    @BindView(R.id.cbFashion) CheckBox mFashionStyle;

    @BindView(R.id.btnSave) Button btnSave;
    private Filter mFilter;
    private DatePickerDialog dueDatePickerDialog;
    private SimpleDateFormat dateFormatter;


   // private String mParam1;

    public FilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment FilterFragment.
     */
     public static FilterFragment newInstance(Filter ft) {
        FilterFragment fragment = new FilterFragment();
     //   Bundle args = new Bundle();
       // args.putString("test", param1);
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
        //btnSave = (Button) view.findViewById(R.id.btnSave);
        //btnCancel = (Button) view.findViewById(R.id.btnCancel);
        //---event handler for the button
        //btnCancel.setOnClickListener(new View.OnClickListener() {
         //   public void onClick(View view) {dismiss();} });
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

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
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        //mName = (EditText) view.findViewById(R.id.etName);
       // mBeginDate = (EditText) view.findViewById(R.id.etDate);
        //mDueDate.setInputType(InputType.TYPE_NULL);
       // mSort = (Spinner) view.findViewById(R.id.spinner);
        //load dropdown list
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSort.setAdapter(adapter);

        if (mFilter == null) {
            mFilter = new Filter();
        } else {
            //mName.setText(mTodo.getName());

            mBeginDate.setText(mFilter.getBeginDate());
            if (mFilter.getSort()!=null) {
                int spinnerPosition = adapter.getPosition(mFilter.getSort());
                mSort.setSelection(spinnerPosition);
            }
        }

        mBeginDate.setOnClickListener(this);
        //solve two click issue for edittext field.
        mBeginDate.setFocusable(false);
        SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy",Locale.US);

        Date pDate=null;
        try {
             if(mFilter.getBeginDate()!=null && !mFilter.getBeginDate().isEmpty())
               pDate  = ft.parse(mFilter.getBeginDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar newCalendar = Calendar.getInstance();
        int year = pDate == null?newCalendar.get(Calendar.YEAR):pDate.getYear();
        int month = pDate ==null?newCalendar.get(Calendar.MONTH):pDate.getMonth();
        int dayofmonth = pDate ==null?newCalendar.get(Calendar.DAY_OF_MONTH):pDate.getDay();

        dueDatePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mBeginDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },year, month, dayofmonth);

        mArts.setChecked(mFilter.getArt());
        mFashionStyle.setChecked(mFilter.getFashionStyle());
        mSports.setChecked(mFilter.getSports());

        // Show soft keyboard automatically and request focus to field
        //mName.requestFocus();

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(view == mBeginDate) {
            dueDatePickerDialog.show();
        }
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}