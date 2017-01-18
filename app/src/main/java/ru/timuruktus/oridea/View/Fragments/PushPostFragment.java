package ru.timuruktus.oridea.View.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


import ru.timuruktus.oridea.R;

public class PushPostFragment extends Fragment implements View.OnClickListener, IPushPostFragment{
    private static final int REQUEST = 1;
    private View rootView;
    ImageView textImage, vkLike;
    TextView titleView, textView, categoryView;
    EditText editText, editTitle;
    Button push, chooseCategory, chooseImage;
    ProgressBar pushLoadingBar;
    private String urlToImage;
    private Bitmap localImg;
    private ArrayList<String> categories;
    PushPostPresenter pushPostPresenter;
    private String category;
    private CharSequence[] categoriesCharSequence;
    public static final String TAG = "tag";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: THIS
        super.onCreate(savedInstanceState);
        rootView =
                inflater.inflate(R.layout.push_post_fragment, container, false);

        pushPostPresenter = new PushPostPresenter(this);

        textImage = (ImageView) rootView.findViewById(R.id.textImage);
        textView = (TextView) rootView.findViewById(R.id.textView);
        categoryView = (TextView) rootView.findViewById(R.id.category);
        titleView = (TextView) rootView.findViewById(R.id.titleView);
        editText = (EditText) rootView.findViewById(R.id.editText);
        editTitle = (EditText) rootView.findViewById(R.id.editTitle);
        push = (Button) rootView.findViewById(R.id.push);
        chooseCategory = (Button) rootView.findViewById(R.id.chooseCategory);
        chooseImage = (Button) rootView.findViewById(R.id.chooseImage);
        pushLoadingBar = (ProgressBar) rootView.findViewById(R.id.pushLoadingBar);
        pushLoadingBar.setVisibility(View.INVISIBLE);

        push.setOnClickListener(this);
        chooseCategory.setOnClickListener(this);
        chooseImage.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.chooseImage){
            // TODO: HEre is an error! Image cannot be read from local repository! Access denied!
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.push_dialog_message)
                    .setTitle(R.string.push_dialog_title);
            builder.setPositiveButton(R.string.push_link_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    localImg = null;
                    createURLLinkDialog();
                }
            });
            builder.setNegativeButton(R.string.push_upload_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    urlToImage = null;
                    chooseIMGFromLocal();
                }
            });
            builder.create();
            builder.show();
        }else if(id == R.id.chooseCategory){
            category = null;
            pushPostPresenter.getCategories();

        }else if(id == R.id.push){
            if(validate()) {
                if(urlToImage == null) {
                    pushPostPresenter.onPushButtonClick(localImg, editText.getText().toString(),
                            editTitle.getText().toString(), categoryView.getText().toString());
                }
                else if(localImg == null){
                    pushPostPresenter.onPushButtonClick(urlToImage, editText.getText().toString(),
                            editTitle.getText().toString(),categoryView.getText().toString());
                }
            }else{
                Toast.makeText(rootView.getContext(),R.string.push_error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean validate(){

        if(urlToImage == null && localImg == null){
            Toast.makeText(rootView.getContext(),R.string.push_image_choose_fail, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editText.getText().toString().equals("")){
            Log.d(TAG, "editText is empty");
            return false;
        }
        if(editTitle.getText().toString().equals("")){
            Log.d(TAG, "editTitle is empty");
            return  false;
        }
        if(category == null){
            Log.d(TAG, "category is empty");
            return false;
        }
        if(categoryView.getText().toString() == ""){
            Log.d(TAG, "categoryView is empty");
            return false;
        }
        return true;
    }

    public void createURLLinkDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_url, null);
        builder.setView(view)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = (EditText) view.findViewById(R.id.push_url_link);
                        //TODO: ERROR ON BUTTON CLICK!
                        urlToImage = et.getText().toString();
                        Picasso.with(rootView.getContext()).load(urlToImage).into(textImage);
                    }
                });
        builder.create();
        builder.show();
    }

    public void chooseIMGFromLocal(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        localImg = null;
        if(resultCode != -1){
            Toast.makeText(rootView.getContext(),R.string.push_didnt_choose_image,Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST && resultCode == -1) {
            Uri selectedImage = data.getData();
            try {
                localImg = MediaStore.Images.Media.getBitmap(rootView.getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            textImage.setImageBitmap(localImg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void turnOnLoading(boolean turn) {
        if(turn) {
            pushLoadingBar.setVisibility(View.VISIBLE);
            textImage.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            categoryView.setVisibility(View.INVISIBLE);
            titleView.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.INVISIBLE);
            editTitle.setVisibility(View.INVISIBLE);
            push.setVisibility(View.INVISIBLE);
            chooseCategory.setVisibility(View.INVISIBLE);
            chooseImage.setVisibility(View.INVISIBLE);
        }else{
            pushLoadingBar.setVisibility(View.INVISIBLE);
            textImage.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            categoryView.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            editTitle.setVisibility(View.VISIBLE);
            push.setVisibility(View.VISIBLE);
            chooseCategory.setVisibility(View.VISIBLE);
            chooseImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError() {
        turnOnLoading(false);
        Toast.makeText(rootView.getContext(), R.string.push_error, Toast.LENGTH_LONG).show();
    }

    public void setCategoryArray(ArrayList<String> categories){
        this.categories = categories;
        showCategoryDialog();
    }

    public void showCategoryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        categoriesCharSequence = categories.toArray(new CharSequence[categories.size()]);
        builder.setTitle(R.string.push_choose_category)
                .setItems(categoriesCharSequence, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        category = categoriesCharSequence[which].toString();
                        categoryView.setText(category);
                    }
                });
        builder.create();
        builder.show();
    }


}
