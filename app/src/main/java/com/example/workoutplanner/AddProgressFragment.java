package com.example.workoutplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.workoutplanner.database.ViewModels.DoneSetViewModel;
import com.example.workoutplanner.database.ViewModels.ProgressViewModel;
import com.example.workoutplanner.database.models.DoneSet;
import com.example.workoutplanner.database.models.Progress;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddProgressFragment extends Fragment {


    static final int REQUEST_IMAGE_CAPTURE = 100;
    private ImageView imageView;
    private EditText progressName;
    private EditText progressDescription;
    private String currentPhotoPath;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_progress, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        progressName = view.findViewById(R.id.progress_name);
        progressDescription = view.findViewById(R.id.progress_description);

        view.findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        ProgressViewModel pvm = ViewModelProviders.of(this).get(ProgressViewModel.class);
        DoneSetViewModel dsvm = ViewModelProviders.of(this).get(DoneSetViewModel.class);


        Progress lastProgress = pvm.getLast();

        View temp = view.findViewById(R.id.coordinator_layout);

        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(progressName.getText().toString().isEmpty()){
                    Snackbar.make(temp, getResources().getString(R.string.add_name), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(progressDescription.getText().toString().isEmpty()){
                    Snackbar.make(temp, getResources().getString(R.string.add_description), Snackbar.LENGTH_LONG).show();
                    return;
                }

                Date start = new Date(0);
                Calendar cal = Calendar.getInstance();
                if (lastProgress != null)
                    start = lastProgress.getDate();

                dsvm.get(start, cal.getTime()).observe(getViewLifecycleOwner(), new Observer<List<DoneSet>>() {
                    @Override
                    public void onChanged(List<DoneSet> doneSets) {
                        Progress p = new Progress(currentPhotoPath, cal.getTime(), progressDescription.getText().toString(), progressName.getText().toString(), doneSets.size(), 0);
                        pvm.insert(p);
                        switchToList();
                    }
                });

                Log.d("MainActivity", lastProgress + "");

//

//                switchToA();
            }
        });
    }

    private void updateSubtitle(){
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(R.string.new_progress_subtitle);
    }

    private void switchToList() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void setPic() {
        Log.d("MainActivity", "Set pic");

        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//

            setPic();

        }

    }
}