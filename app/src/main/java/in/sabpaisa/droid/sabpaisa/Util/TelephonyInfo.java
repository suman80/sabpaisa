package in.sabpaisa.droid.sabpaisa.Util;

/**
 * Created by abc on 13-06-2017.
 */

import java.lang.reflect.Method;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


public class TelephonyInfo {

    private static TelephonyInfo telephonyInfo;
    private String imeiSIM1;
    private String imeiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;


    public boolean isDualSIM() {
        return imeiSIM2 != null;
    }

    private TelephonyInfo() {
    }
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public static TelephonyInfo getInstance(Context context) {

        if (telephonyInfo == null) {

            telephonyInfo = new TelephonyInfo();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

            telephonyInfo.imeiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imeiSIM2 = null;

        }

        return telephonyInfo;
    }

    public static String getOutput(Context context, String methodName, int slotId) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        String reflectionMethod = null;
        String output = null;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            for (Method method : telephonyClass.getMethods()) {
                String name = method.getName();
                if (name.contains(methodName)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && params[0].getName().equals("int")) {
                        reflectionMethod = name;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (reflectionMethod != null) {
            try {
                output = getOpByReflection(telephony, reflectionMethod, slotId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    private static String getOpByReflection(TelephonyManager telephony, String predictedMethodName, int slotID, boolean isPrivate) {

        //Log.i("Reflection", "Method: " + predictedMethodName+" "+slotID);
        String result = null;

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID;
            if (slotID != -1) {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName, parameter);
                    Log.wtf("SIMID1", String.valueOf(getSimID));
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
                    Log.wtf("SIMID", String.valueOf(getSimID));
                }
            } else {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName);
                    Log.wtf("SIMID3", String.valueOf(getSimID));
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName);
                    Log.wtf("SIMID4", String.valueOf(getSimID));
                }
            }

            Object ob_phone;
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            if (getSimID != null) {
                if (slotID != -1) {
                    ob_phone = getSimID.invoke(telephony, obParameter);
                } else {
                    ob_phone = getSimID.invoke(telephony);
                }

                if (ob_phone != null) {
                    result = ob_phone.toString();

                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        //Log.i("Reflection", "Result: " + result);
        return result;
    }
}
