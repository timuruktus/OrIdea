package ru.timuruktus.oridea.Presenter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ru.timuruktus.oridea.Events.Global.ShowErrorEvent;
import ru.timuruktus.oridea.Events.Global.ShowLoadingBarEvent;
import ru.timuruktus.oridea.Events.ToAuthPresenter.AuthFailedEvent;
import ru.timuruktus.oridea.Events.ToAuthPresenter.AuthSucceedEvent;
import ru.timuruktus.oridea.Events.ToAuthPresenter.OnLoginButtonClickEvent;
import ru.timuruktus.oridea.Events.ToAuthPresenter.OnRegButtonClickEvent;
import ru.timuruktus.oridea.Events.ToEmailAuth.StartLogEvent;
import ru.timuruktus.oridea.Events.ToEmailAuth.StartRegEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.ChangeFragmentEvent;
import ru.timuruktus.oridea.Events.ToMainActivity.HideLogoutEvent;
import ru.timuruktus.oridea.View.Fragments.WelcomeFragment;

public class AuthPresenter {

    public AuthPresenter(){
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onRegButtonClick(OnRegButtonClickEvent event){
        EventBus.getDefault().post(new ShowLoadingBarEvent(true));
        EventBus.getDefault().post(new StartRegEvent(event.email, event.pass));
    }

    @Subscribe
    public void onLoginButtonClick(OnLoginButtonClickEvent event){
        EventBus.getDefault().post(new ShowLoadingBarEvent(true));
        EventBus.getDefault().post(new StartLogEvent(event.email, event.pass));
    }

    @Subscribe
    public void onAuthFailedEvent(AuthFailedEvent event){
        EventBus.getDefault().post(new ShowErrorEvent());
    }

    @Subscribe
    public void onAuthSuccess(AuthSucceedEvent event){
        EventBus.getDefault().post(new ChangeFragmentEvent(new WelcomeFragment(), false));
        EventBus.getDefault().post(new HideLogoutEvent(false));
    }
}
