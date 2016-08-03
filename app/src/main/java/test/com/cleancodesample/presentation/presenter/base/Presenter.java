package test.com.cleancodesample.presentation.presenter.base;

/**
 * This is an interface that defines specific actions that will be triggered from the hosting
 * view.
 * TODO: We can add more actions like onError or showMessage ... etc
 */
public interface Presenter {
    void resume();
    void pause();
    void stop();
    void destory();
}
