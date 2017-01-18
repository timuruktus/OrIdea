package ru.timuruktus.oridea.View.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import ru.timuruktus.oridea.R;
import ru.timuruktus.oridea.View.Activities.MainActivity;

public class AuthFragment extends Fragment implements View.OnClickListener{

    private TextView editTextEmail, editTextPass;
    private SliderLayout sliderShow;
    private Button regBut, logBut;
    private ProgressBar loadingBar;
    private TextInputLayout emailInput, passInput;
    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView =
                inflater.inflate(R.layout.auth_fragment, container, false);

        IMainActivity iMainActivity = (MainActivity) getActivity();
        iMainActivity.changeToolbarTitle(R.string.title_activity_auth);

        String custom_font = "fonts/Roboto-Italic.ttf";
        Typeface CF = Typeface.createFromAsset(getActivity().getAssets(), custom_font);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.INVISIBLE);

        regBut = (Button) rootView.findViewById(R.id.regBut);
        regBut.setOnClickListener(this);
        regBut.setTypeface(CF);
        logBut = (Button) rootView.findViewById(R.id.logBut);
        logBut.setOnClickListener(this);
        logBut.setTypeface(CF);

        emailInput = (TextInputLayout) rootView.findViewById(R.id.emailInput);
        passInput = (TextInputLayout) rootView.findViewById(R.id.passInput);
        emailInput.setTypeface(CF);
        passInput.setTypeface(CF);

        editTextEmail = (TextView) rootView.findViewById(R.id.edit_text_email);
        editTextPass = (TextView) rootView.findViewById(R.id.edit_text_pass);
        editTextEmail.setTypeface(CF);
        editTextPass.setTypeface(CF);

        sliderShow = (SliderLayout) rootView.findViewById(R.id.slider);
        configureSliderImages();

        return rootView;
    }

    /**
     * Called when fragment starts
     * !!DON'T CALL IT ANYWHERE ELSE!!
     */
    public void configureSliderImages(){
        TextSliderView textSliderView = new TextSliderView(getActivity());
        textSliderView.image(R.drawable.img1);
        sliderShow.addSlider(textSliderView);
        TextSliderView textSliderView2 = new TextSliderView(getActivity());
        textSliderView2.image(R.drawable.img2);
        sliderShow.addSlider(textSliderView2);
        TextSliderView textSliderView3 = new TextSliderView(getActivity());
        textSliderView3.image(R.drawable.img3);
        sliderShow.addSlider(textSliderView3);
        TextSliderView textSliderView4 = new TextSliderView(getActivity());
        textSliderView4.image(R.drawable.img4);
        sliderShow.addSlider(textSliderView4);
        sliderShow.startAutoCycle(10000,10000,true);
        sliderShow.setPresetTransformer("Stack"); //stack, tablet- good
    }


    /**
     * Changes a fragment
     * @param isItWasLogin - depends on what text will be shown in Toast
     *                     if "true" - you were signIn with login
     *                     if "false" - you were created your account
     */
    @Override
    public void showChangeFragment(boolean isItWasLogin) {
        showLoadingBar(false);
        if(isItWasLogin) {
            Log.d("tag", "changeFragment(itWasLogin == true)");
            Toast.makeText(getActivity().getApplicationContext(), R.string.auth_success_login,
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.d("tag", "changeFragment(itWasLogin == false)");
            Toast.makeText(getActivity().getApplicationContext(), R.string.auth_success_singin,
                    Toast.LENGTH_SHORT).show();
        }
        FragmentManager fragmentManager = getFragmentManager();
        authPresenter.onChangeFragment(fragmentManager);

    }

    /**
     * Shows an error Toast message and make loading bar invisible
     *
     */
    @Override
    public void showRegError() {
        Context context = getActivity().getApplicationContext();
        Toast.makeText(context, R.string.auth_failed, Toast.LENGTH_SHORT).show();
        showLoadingBar(false);
    }

    /**
     * Shows start/end of loading
     * @param show - "true" if loading was started (NEEDED TO SHOW LOADING BAR)
     */
    @Override
    public void showLoadingBar(boolean show) {
        if(show) {
            emailInput.setVisibility(View.INVISIBLE);
            passInput.setVisibility(View.INVISIBLE);
            regBut.setVisibility(View.INVISIBLE);
            logBut.setVisibility(View.INVISIBLE);
            loadingBar.setVisibility(View.VISIBLE);
        }else{
            emailInput.setVisibility(View.VISIBLE);
            passInput.setVisibility(View.VISIBLE);
            regBut.setVisibility(View.VISIBLE);
            logBut.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * CLICK LISTENER
     * @param v - View
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.regBut){
            authPresenter.onRegButClick(editTextEmail.getText().toString(), editTextPass.getText().toString());
        }
        else if(id == R.id.logBut){
            authPresenter.onLoginButClick(editTextEmail.getText().toString(), editTextPass.getText().toString());
        }
    }

    /**
     * Called, when Fragment Stopped
     */
    @Override
    public void onStop(){
        sliderShow.stopAutoCycle();
        super.onStop();
    }

}
