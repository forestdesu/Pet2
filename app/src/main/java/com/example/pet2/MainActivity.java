package com.example.pet2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    EditText petID;
    ImageView imgPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
        petID = (EditText) findViewById(R.id.petID);
        imgPet = (ImageView) findViewById(R.id.imageView);
    }

    public void onClick(View view) {
        mTextView.setText("");
        PetAPI petAPI = PetAPI.retrofit.create(PetAPI.class);
        // часть слова
        final Call<Pet> call =
                petAPI.getPet(petID.getText().toString());

        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    Pet result = response.body();
                    String Tags = "";
                    for (Tags i:result.getTags()) {
                        Tags = Tags + "\nTag id: " + i.getId() +
                                "\nTag name: " + i.getName();
                    }
                    Picasso.get().load(result.getPhotoUrls().get(0)).into(imgPet);
                    mTextView.setText("Id: " + result.getId().toString() +
                            "\nCategory id: " + result.getCategory().getId() +
                            "\nCategory name: " + result.getCategory().getName() +
                            "\nName: " + result.getName() +
                            "\nPhotoUrls: " + result.getPhotoUrls() + Tags +
                            "\nStatus: " + result.getStatus());
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }
}