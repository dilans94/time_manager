package com.timemanager;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    View view;
    ListView lv;
    ListViewAdapter listAdapter;
    ArrayList<ActivitiesModal> alActivities = new ArrayList<>();
    SQLiteDB sqLiteDB;
    TextView tv;
    Button tv_log, tv_settings;
    public static ActivitiesModal activitiesModal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteDB = new SQLiteDB(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, null);
        lv = (ListView) view.findViewById(R.id.listView);
        tv = (TextView) view.findViewById(R.id.tv);
        lv.setOnItemClickListener(this);
        activitiesModal = new ActivitiesModal();
        activitiesModal.setTitle("title");
        /*Intent intent=new Intent(getActivity(),CommonClass.class);
        intent.putExtra("activity_log",activitiesModal);
        startActivity(intent);*/
        tv_log = (Button) view.findViewById(R.id.log_tv);
        tv_settings = (Button) view.findViewById(R.id.setting_tv);
        tv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitiesModal = null;
                Intent intent = new Intent(getActivity(), CommonClass.class);
                startActivity(intent);
            }
        });
        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitiesModal = null;
                Intent newAct = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(newAct);
            }
        });
        alActivities.clear();
        Cursor cursor = sqLiteDB.returnActivityLog();
        if (cursor != null && cursor.getCount() > 0) {
            tv.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            cursor.moveToFirst();
            do {
                ActivitiesModal modal = new ActivitiesModal();
                modal.setId(cursor.getInt(cursor.getColumnIndex("id")));

                modal.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                modal.setDate(cursor.getString(cursor.getColumnIndex("date")));
                modal.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                modal.setActivity_type(cursor.getString(cursor.getColumnIndex("activity_type")));
                modal.setTotal_duration(cursor.getString(cursor.getColumnIndex("duration")));
                modal.setUser_comment(cursor.getString(cursor.getColumnIndex("comment")));
                modal.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
                alActivities.add(modal);

            }
            while (cursor.moveToNext());
            listAdapter = new ListViewAdapter(getActivity(), alActivities);
            lv.setAdapter(listAdapter);
        } else {
            tv.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CommonClass.class);
        activitiesModal = alActivities.get(position);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
