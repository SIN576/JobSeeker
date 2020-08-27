package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityViewCVDetailBinding;

import java.io.File;
import java.util.List;

public class ViewCVDetailActivity extends AppCompatActivity {

    ActivityViewCVDetailBinding binding;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCVDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id = getIntent().getIntExtra("id",1);
        Log.e("id",id+"");

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BaseMultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        super.onPermissionsChecked(multiplePermissionsReport);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        super.onPermissionRationaleShouldBeShown(list, permissionToken);
                    }
                }).check();

        binding.progressBar.setVisibility(View.VISIBLE);
        FileLoader.with(this)
                .load(Connection.BASEURL+"/api/postcv/showPdf/"+id)
                .fromDirectory("PDFFiles",FileLoader.DIR_EXTERNAL_PUBLIC)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        File filePDF = response.getBody();
                        binding.pdfView.fromFile(filePDF)
                                // .pages(0, 2, 1, 3, 3, 3)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .load();
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.e("error: ",t.getMessage());
                    }
                });
    }
}