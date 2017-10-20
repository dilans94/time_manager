package com.timemanager;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



public class UserProfileFragment extends Fragment implements View.OnClickListener {
    EditText userName, userEmail, userGender, userComment;
    Button btnSave;
    View v;
    SQLiteDB sql;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_profile, null);

        userName = (EditText) v.findViewById(R.id.userName);
        userComment = (EditText) v.findViewById(R.id.userComment);
        sql = new SQLiteDB(getActivity());

        userEmail = (EditText) v.findViewById(R.id.userEmail);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        userGender = (EditText) v.findViewById(R.id.userGender);
        btnSave.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSave)
        {
            String uName=userName.getText().toString();
            String uEmail=userEmail.getText().toString();
            String uComment=userComment.getText().toString();
            String uGender=userGender.getText().toString();
            UserProfile profileModal = new UserProfile();
            profileModal.setU_name(uName);
            profileModal.setU_gender(uGender);
            profileModal.setU_email(uEmail);
            profileModal.setU_comment(uComment);
            sql.generateUser(profileModal);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
}
