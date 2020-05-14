package com.cf.rnhms.scan;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import org.json.JSONObject;

public class ReactNativeHmsScanModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private static final int IMAGE_PICKER_REQUEST = 1;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_SCANNER_CANCELLED = "E_SCANNER_CANCELLED";
    private static final String E_FAILED_TO_SHOW_SCANNER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_INVALID_CODE = "E_INVALID_CODE";
    private static final int REQUEST_CODE_SCAN = 567;
    private Promise mPickerPromise;

    public ReactNativeHmsScanModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "ReactNativeHmsScan";
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == REQUEST_CODE_SCAN) {
                if (mPickerPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        mPickerPromise.reject(E_SCANNER_CANCELLED, "Scanner was cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
                        Object obj = intent.getParcelableExtra(ScanUtil.RESULT);
                        if (obj instanceof HmsScan) {
                            if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                                mPickerPromise.resolve(((HmsScan) obj).getOriginalValue().toString());
                            } else {
                                mPickerPromise.reject(E_INVALID_CODE, "Invalid Code");
                            }
                            return;
                        }
                    }
                }
            }
        }

    };

    @ReactMethod
    public void startScan(final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        // Store the promise to resolve/reject when picker returns data
        mPickerPromise = promise;

        try {
            ScanUtil.startScan(currentActivity, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        } catch (Exception e) {
            mPickerPromise.reject(E_FAILED_TO_SHOW_SCANNER, e);
            mPickerPromise = null;
        }
    }
}
