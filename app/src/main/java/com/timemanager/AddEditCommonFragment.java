package com.timemanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;



public class AddEditCommonFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View view;
    EditText et_Title, et_place, et_comment, et_duration, et_date;
    Button btn_camera, btn_save, btn_delete, btn_cancel, btn_map;
    Spinner type_spinner;
    ImageView work_image;
    FusedLocationProviderClient fusedLocationClient;
    Location locations;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    String latitude="", longitude="";
    String[] type = {"Work", "Leisure", "Study", "Sports"};
    SQLiteDB sqLiteDB;
    LocationManager manager;
    Location location;
    boolean isGPS = false;
    String activity_type = "";
    String path="";
    String currentAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteDB = new SQLiteDB(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_common, null);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        manager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        type_spinner = (Spinner) view.findViewById(R.id.type_spinner);
        type_spinner.setOnItemSelectedListener(this);
        et_Title = (EditText) view.findViewById(R.id.title_et);
        et_date = (EditText) view.findViewById(R.id.date_et);
        et_place = (EditText) view.findViewById(R.id.place_et);
        et_duration = (EditText) view.findViewById(R.id.duration_et);
        et_comment = (EditText) view.findViewById(R.id.comment_et);
        work_image = (ImageView) view.findViewById(R.id.work_image);
        btn_camera = (Button) view.findViewById(R.id.camera_btn);
        btn_save = (Button) view.findViewById(R.id.save_btn);
        btn_delete = (Button) view.findViewById(R.id.delete_btn);
        btn_cancel = (Button) view.findViewById(R.id.cancel_btn);
        btn_map = (Button) view.findViewById(R.id.map_btn);
        btn_save.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_map.setOnClickListener(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e("Location", "Location");
                latitude = locationResult.getLastLocation().getLatitude()+"";
                longitude = locationResult.getLastLocation().getLongitude()+"";
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address returnedAddress = addresses.get(0);
                    currentAddress=returnedAddress.getAddressLine(0);
                    et_place.setText(currentAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        ArrayAdapter arrayadapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, type);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(arrayadapter);

        if (!isGPS) {


            showAlert();
        } else {
            updateParams();
        }

        if (ListFragment.activitiesModal == null) {
            btn_delete.setVisibility(View.GONE);
            btn_map.setVisibility(View.GONE);

        } else {
            btn_delete.setVisibility(View.VISIBLE);
            btn_map.setVisibility(View.VISIBLE);
            et_Title.setText(ListFragment.activitiesModal.getTitle());
            et_place.setText(ListFragment.activitiesModal.getPlace());

            et_date.setText(ListFragment.activitiesModal.getDate());
            ;
            et_duration.setText(ListFragment.activitiesModal.getTotal_duration());
            et_comment.setText(ListFragment.activitiesModal.getUser_comment());
            et_place.setText(ListFragment.activitiesModal.getPlace());
            path=ListFragment.activitiesModal.getPhoto();
            File imgFile = new  File(ListFragment.activitiesModal.getPhoto());

            if(imgFile.exists()){

                Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                work_image.setImageBitmap(bmp);

            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                break;
            case R.id.save_btn:
                if(ListFragment.activitiesModal==null)
                {
                    if(latitude.equalsIgnoreCase(""))
                    {
                        Toast.makeText(getActivity(), "Fetching Location", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ActivitiesModal activitiesModal = new ActivitiesModal();
                        String title = et_Title.getText().toString().trim();
                        String place = et_place.getText().toString().trim();
                        String date = et_date.getText().toString().trim();
                        String duration = et_duration.getText().toString().trim();
                        String comment = et_comment.getText().toString().trim();

                        activitiesModal.setTitle(title);
                        activitiesModal.setDate(date);
                        activitiesModal.setTotal_duration(duration);
                        activitiesModal.setPlace(place);
                        activitiesModal.setUser_comment(comment);
                        activitiesModal.setActivity_type(activity_type);
                        activitiesModal.setPhoto(path);
                        activitiesModal.setLatitude(latitude);
                        activitiesModal.setLongitude(longitude);
                        sqLiteDB.createActivityLog(activitiesModal);
                        Intent main = new Intent(getActivity(), MainActivity.class);
                        startActivity(main);
                    }


                }
                else
                {
                    ActivitiesModal activitiesModal = new ActivitiesModal();
                    String title = et_Title.getText().toString().trim();
                    String place = et_place.getText().toString().trim();
                    String date = et_date.getText().toString().trim();
                    String duration = et_duration.getText().toString().trim();
                    String comment = et_comment.getText().toString().trim();

                    activitiesModal.setTitle(title);
                    activitiesModal.setDate(date);
                    activitiesModal.setTotal_duration(duration);
                    activitiesModal.setPlace(place);
                    activitiesModal.setUser_comment(comment);
                    activitiesModal.setActivity_type(activity_type);
                    activitiesModal.setPhoto(path);
                    activitiesModal.setLatitude(latitude);
                    activitiesModal.setLongitude(longitude);
                    sqLiteDB.updateLog(activitiesModal,ListFragment.activitiesModal.getId());
                    Intent main = new Intent(getActivity(), MainActivity.class);
                    startActivity(main);
                }


                break;
            case R.id.delete_btn:
                if(ListFragment.activitiesModal==null)
                {
                        btn_delete.setVisibility(View.GONE);
                }
                else
                {
                    btn_delete.setVisibility(View.VISIBLE);

                    sqLiteDB.deleteLog(ListFragment.activitiesModal.getId());
                    Intent i=new Intent(getActivity(),MainActivity.class);
                    startActivity(i);                }
                break;
            case R.id.cancel_btn:
                Intent i=new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                break;
            case R.id.map_btn:
                if(ListFragment.activitiesModal==null)
                {
                    btn_map.setVisibility(View.GONE);
                }
                else
                {
                    Intent iMap=new Intent(getActivity(),MyLocationActivity.class);
                    iMap.putExtra("latitude",latitude);
                    iMap.putExtra("longitude",longitude);
                    startActivity(iMap);
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activity_type = type[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





               /* latitude=location.getLatitude()+"";
                longitude=location.getLongitude()+"";
                */






    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                getActivity().finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File dest = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            path = dest.getAbsolutePath();
            FileOutputStream fo;
            try {
                dest.createNewFile();
                fo = new FileOutputStream(dest);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            work_image.setImageBitmap(bmp);
        }
    }
    void updateParams() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient sClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> t = sClient.checkLocationSettings(builder.build());
        t.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locUpdate();
            }
        });
    }
    public void locUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    @Override
    public void onStop() {
        super.onStop();


    }



}
