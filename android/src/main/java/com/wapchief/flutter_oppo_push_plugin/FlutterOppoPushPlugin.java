package com.wapchief.flutter_oppo_push_plugin;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.heytap.msp.push.mode.ErrorCode;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterOppoPushPlugin
 */
public class FlutterOppoPushPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Activity mActivity;

    private Application mApplication;

    private static String TAG = "FlutterOppoPushPlugin";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        mApplication = (Application) flutterPluginBinding.getApplicationContext();

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_oppo_push_plugin");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("initSDK")) {
            if (!android.os.Build.BRAND.toLowerCase().contains("oppo")) {
                Log.d(TAG, "not oppo mobile devices !!!");
                return;
            }
            HeytapPushManager.init(mApplication, call.argument("isDebug"));
            HeytapPushManager.register(mApplication, call.argument("key"), call.argument("secret"),
                    new ICallBackResultService() {
                        @Override
                        public void onRegister(int i, String s) {
                            if (i == ErrorCode.SUCCESS) {
                                //注册成功
                                Log.d(TAG, "----oppo 注册成功，registerId=" + s);
                            } else {
                                //注册失败
                                Log.d(TAG, "----oppo 注册失败" + s);
                                // 如果第一次注册失败,第二次可以直接调用HeytapPushManager.getRegister()进行重试,此方法默认会使用第一次传入的参数掉调用注册。
                                HeytapPushManager.getRegister();
                            }
                        }

                        @Override
                        public void onUnRegister(int i) {

                        }

                        @Override
                        public void onSetPushTime(int i, String s) {

                        }

                        @Override
                        public void onGetPushStatus(int i, int i1) {

                        }

                        @Override
                        public void onGetNotificationStatus(int i, int i1) {

                        }
                    });
        } else if (call.method.equals("getPushStatus")) {

//            channel.invokeMethod("getPushStatus",);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
