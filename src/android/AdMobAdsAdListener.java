package com.appfeel.cordova.admob;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

@SuppressLint("DefaultLocale")
public class AdMobAdsAdListener extends AdListener {
  private String adType = "";
  private AdMobAds admobAds;
  private IAdLoadedAvailable iAdLoadedAvailable;

  public interface IAdLoadedAvailable {
    void onAdLoaded(String adType);

    void onAdOpened(String adType);
  }

  public AdMobAdsAdListener(String adType, AdMobAds admobAds, IAdLoadedAvailable iAdLoadedAvailable) {
    this.adType = adType;
    this.admobAds = admobAds;
    this.iAdLoadedAvailable = iAdLoadedAvailable;
  }

  @Override
  public void onAdLoaded() {
    iAdLoadedAvailable.onAdLoaded(adType);
    Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad loaded");
    String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdLoaded, { 'adType': '%s' });", adType);
    admobAds.webView.loadUrl(event);
  }

  @Override
  public void onAdFailedToLoad(int errorCode) {
    String reason = getErrorReason(errorCode);
    Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": fail to load ad (" + reason + ")");
    String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdFailedToLoad, { 'adType': '%s', 'error': %d, 'reason': '%s' });", adType, errorCode, reason);
    admobAds.webView.loadUrl(event);
  }

  /** Gets a string error reason from an error code. */
  public String getErrorReason(int errorCode) {
    String errorReason = "Unknown";
    switch (errorCode) {
    case AdRequest.ERROR_CODE_INTERNAL_ERROR:
      errorReason = "Internal error";
      break;
    case AdRequest.ERROR_CODE_INVALID_REQUEST:
      errorReason = "Invalid request";
      break;
    case AdRequest.ERROR_CODE_NETWORK_ERROR:
      errorReason = "Network Error";
      break;
    case AdRequest.ERROR_CODE_NO_FILL:
      errorReason = "No fill";
      break;
    }
    return errorReason;
  }

  @Override
  public void onAdOpened() {
    iAdLoadedAvailable.onAdOpened(adType);
    Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad opened");
    String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdOpened, { 'adType': '%s' });", adType);
    admobAds.webView.loadUrl(event);
  }

  @Override
  public void onAdLeftApplication() {
    Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": left application");
    String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdLeftApplication, { 'adType': '%s' });", adType);
    admobAds.webView.loadUrl(event);
  }

  @Override
  public void onAdClosed() {
    Log.d(AdMobAds.ADMOBADS_LOGTAG, adType + ": ad closed after clicking on it");
    String event = String.format("javascript:cordova.fireDocumentEvent(admob.events.onAdClosed, { 'adType': '%s' });", adType);
    admobAds.webView.loadUrl(event);
  }
}
