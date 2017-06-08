package com.djay.locatesecurly.utils.bridges;

/**
 * Interface for showing progress bar when background process happens
 *
 * @author Dhananjay Kumar
 */
@SuppressWarnings("unused")
public interface LoadingBridge {

    void showProgress();

    void hideProgress();
}