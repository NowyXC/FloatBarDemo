package com.example.firstapp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;


import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Nowy on 2017/12/20.
 * 方法归纳
 * # 获取手机型号
 * # 是否有SDCard
 * # 获取Cpu内核数
 * # 判断手机是否处理睡眠
 * # 检查网络是否已连接
 * # 判断是否为手机
 * # 获得设备的屏幕宽度
 * # 获得设备的屏幕高度
 * # 获取设备id（IMEI）(需要权限)
 * # 拨打电话，直接拨打(需要权限)
 * # 拨打电话，跳转到拨号界面（不需要权限的）
 * # 发送短信息，跳转到发送短信界面
 * # 获取联系人集合 (需要权限)
 * # 把数据写入到系统的联系人(需要权限)
 * # 收集设备信息（反射）
 *
 */

public class DeviceUtil {
    public static final String TAG = DeviceUtil.class.getSimpleName();
    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";


    private static final String[] CONTACTOR_ION = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };


    /**
     * 获取手机型号
     */
    public String getPhoneModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取手机sim卡号
     *
     * @param context
     */
    public String getPhoneSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return tm.getSubscriberId();
        }
        return null;
    }


    /**
     * 获取手机号
     *
     * @param context
     */
    public String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return tm.getLine1Number();
        }
        return null;
    }


    /**
     * 是否有SDCard
     *
     * @return
     */
    public static boolean haveSDCard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取sd卡剩余空间的大小
     */
    public long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long freeBlocks = sf.getAvailableBlocks();// 空闲的数据块的数量
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取sd卡空间的总大小
     */
    @SuppressWarnings("deprecation")
    public long getSDAllSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long allBlocks = sf.getBlockCount(); // 获取所有数据块数
        // 返回SD卡大小
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }


    /**
     * 获取手机内安装的应用
     */
    public List<PackageInfo> getInstalledApp(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);
    }


    /**
     * 获取手机安装非系统应用
     */
    @SuppressWarnings("static-access")
    public List<PackageInfo> getUserInstalledApp(Context context) {
        List<PackageInfo> infos = getInstalledApp(context);
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        for (PackageInfo info : infos) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(info);
            }
        }
        infos.clear();
        infos = null;
        return apps;
    }


    /**
     * 获取安装应用的信息
     */
    public Map<String, Object> getInstalledAppInfo(Context context,
                                                   PackageInfo info) {
        Map<String, Object> appInfos = new HashMap<String, Object>();
        PackageManager pm = context.getPackageManager();
        ApplicationInfo aif = info.applicationInfo;
        appInfos.put("icon", pm.getApplicationIcon(aif));
        appInfos.put("lable", pm.getApplicationLabel(aif));
        appInfos.put("packageName", aif.packageName);
        return appInfos;
    }

    /**
     * 打开指定包名的应用
     */
    public void startAppPkg(Context context, String pkg) {
        Intent startIntent = context.getPackageManager()
                .getLaunchIntentForPackage(pkg);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
    }

    /**
     * 卸载指定包名的应用
     */
    public void unInstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
    }


    /**
     * 获取Cpu内核数
     * @return
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }

            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 判断手机是否处理睡眠
     *
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        Log.d(TAG,isSleeping ? "手机睡眠中.." : "手机未睡眠...");
        return isSleeping;
    }

    /**
     * 检查网络是否已连接
     *
     * @param context
     * @return
     * @author com.tiantian
     */
    @SuppressLint("MissingPermission")
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断是否为手机
     *
     * @param context
     * @return
     * @author wangjie
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            Log.i(TAG,"Current device is Tablet!");
            return false;
        } else {
            Log.i(TAG,"Current device is phone!");
            return true;
        }
    }

    /**
     * 获得设备的屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    /**
     * 获得设备的屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }


    /**
     * 获取设备id（IMEI）
     * 需要授权Manifest.permission.READ_PHONE_STATE
     * @param appContext 请使用Application context
     * @return "" & String
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getDeviceIMEI(Context appContext) {
        String deviceId = "";
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //未获取权限返回空字符串
            if (isPhone(appContext)) {
                TelephonyManager telephony = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = telephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        Log.d(TAG,"当前设备IMEI码: " + deviceId);
        return deviceId;
    }


    /**
     * 拨打电话，直接拨打，需要权限
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
            return;
        }
    }

    /**
     * 拨打电话，跳转到拨号界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }


    /**
     * 发送短信息，跳转到发送短信界面
     *
     * @param context
     * @param phoneNumber
     * @param content
     */
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtil.isEmpty(phoneNumber) ? "" : phoneNumber));
        //Uri uri = Uri.parse("smsto:"); //不填写收件人
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtil.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }


    /**
     * 获取联系人集合
     * @param context
     * @return 电话号码
     */
    public static HashMap getContacts(Context context) {
        HashMap map = new HashMap();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return map;
        }

        Cursor phones = null;
        ContentResolver cr = context.getContentResolver();
        try {
            phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, CONTACTOR_ION, null, null, "sort_key");
            if (phones != null) {
                final int contactIdIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                final int displayNameIndex = phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneString, displayNameString, contactIdString;
                while (phones.moveToNext()) {
                    phoneString = phones.getString(phoneIndex);
                    displayNameString = phones.getString(displayNameIndex);
                    contactIdString = phones.getString(contactIdIndex);
//                    L.e("GetPhoneContactUtils", "电话=" + phoneString);
                    Log.e(TAG,"名字=" + displayNameString);
//                    L.e("GetPhoneContactUtils", "联系人id=" + contactIdString);
                    map.put(phoneString, displayNameString);
                }
                Log.e(TAG, "联系人总数=" + map.size());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (phones != null)
                phones.close();
        }

        return map;
    }


    /**
     * 把数据写入到系统的联系人
     * @param context
     * @param name
     * @param phone
     */
    public static void insertContacts(Context context, String name, String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 把数据写入到系统的联系人.
        ContentResolver resolver = context.getContentResolver();
        // ----------在raw_contant表中添加一条新的id---------------
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        // 插入联系人 必须要知道 新的联系人的id
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, "contact_id");
        int contact_id;
        if (cursor.moveToLast()) {
            contact_id = cursor.getInt(0) + 1; // 数据库里面有数据 最后一条联系人的id + 1
        } else {// 原先数据库是空的 从第一个联系人开始
            contact_id = 1;
        }
        ContentValues values = new ContentValues();
        values.put("contact_id", contact_id);
        resolver.insert(uri, values);

        // ------------在data表里面 添加id对应的数据-------------
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        // 插入姓名
        ContentValues nameValue = new ContentValues();
        nameValue.put("data1", name);
        nameValue.put("raw_contact_id", contact_id);
        nameValue.put("mimetype", "vnd.android.cursor.item/name");
        resolver.insert(dataUri, nameValue);

        // 插入电话
        ContentValues phoneValue = new ContentValues();
        phoneValue.put("data1", phone);
        phoneValue.put("raw_contact_id", contact_id);
        phoneValue.put("mimetype", "vnd.android.cursor.item/phone_v2");
        resolver.insert(dataUri, phoneValue);

        Log.e(TAG, "插入数据成功=" + name);

    }


    /**
     * 获取移动终端类型
     *
     * @param context 上下文
     * @return 手机制式
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
     * </ul>
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }



    /**
     * 获取手机状态信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @param context 上下文
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    public static String getPhoneStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
            str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
            str += "Line1Number = " + tm.getLine1Number() + "\n";
            str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
            str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
            str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
            str += "NetworkType = " + tm.getNetworkType() + "\n";
            str += "honeType = " + tm.getPhoneType() + "\n";
            str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
            str += "SimOperator = " + tm.getSimOperator() + "\n";
            str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
            str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
            str += "SimState = " + tm.getSimState() + "\n";
            str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
            str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
            return str;
        }

        return str;
    }






    /**
     * 收集设备信息
     *
     * @param context
     */
    public static Properties collectDeviceInfo(Context context) {
        Properties mDeviceCrashInfo = new Properties();
        try {
            // Class for retrieving various kinds of information related to the
            // application packages that are currently installed on the device.
            // You can find this class through getPackageManager().
            PackageManager pm = context.getPackageManager();
            // getPackageInfo(String packageName, int flags)
            // Retrieve overall information about an application package that is installed on the system.
            // public static final int GET_ACTIVITIES
            // Since: API Level 1 PackageInfo flag: return information about activities in the package in activities.
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                // public String versionName The version name of this package,
                // as specified by the <manifest> tag's versionName attribute.
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                // public int versionCode The version number of this package,
                // as specified by the <manifest> tag's versionCode attribute.
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,"Error while collect package info", e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
//                if (DEBUG) {
                Log.d(TAG,field.getName() + " : " + field.get(null));
//                }
            } catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }

        return mDeviceCrashInfo;
    }

    /**
     * 收集设备信息
     *
     * @param context
     * @return
     */
    public static String collectDeviceInfoStr(Context context) {
        Properties prop = collectDeviceInfo(context);
        Set deviceInfos = prop.keySet();
        StringBuilder deviceInfoStr = new StringBuilder("{\n");
        for (Iterator iter = deviceInfos.iterator(); iter.hasNext(); ) {
            Object item = iter.next();
            deviceInfoStr.append("\t\t\t" + item + ":" + prop.get(item) + ", \n");
        }
        deviceInfoStr.append("}");
        return deviceInfoStr.toString();
    }


}
