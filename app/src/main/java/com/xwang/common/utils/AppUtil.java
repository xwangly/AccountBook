package com.xwang.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xwangly on 2017/2/9.
 */

public class AppUtil {
    private static final String COMMAND_MEMINFO = "/proc/meminfo";
    private static final String COMMAND_TOP = "/system/bin/top";
    private static final String COMMAND_KILL_9 = "kill -9";
    private static final String PERMISSION_BOOT_COMPLETED = "android.permission.RECEIVE_BOOT_COMPLETED";
    public static List<String[]> mProcessList = null;

    public AppUtil() {
    }

    public static String getVersion(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo info = e.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception var3) {
            var3.printStackTrace();
            return "1.0";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo info = e.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0;
        }
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List servicesList = am.getRunningServices(Integer.MAX_VALUE);
        Iterator var5 = servicesList.iterator();

        while(var5.hasNext()) {
            ActivityManager.RunningServiceInfo si = (ActivityManager.RunningServiceInfo)var5.next();
            if(className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent = null;
        boolean ret = false;

        try {
            intent = new Intent(context, Class.forName(className));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if(intent != null) {
            ret = context.stopService(intent);
        }

        return ret;
    }

    public static int getNumCores() {
        try {
            File e = new File("/sys/devices/system/cpu/");
            File[] files = e.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 1;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager e = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(e != null) {
                NetworkInfo info = e.getActiveNetworkInfo();
                if(info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

            return false;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isMobile(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.getType() == 0;
    }

    public static boolean importDatabase(Context context, String dbName, int rawRes) {
        short bufferSize = 1024;
        InputStream is = null;
        FileOutputStream fos = null;
        boolean flag = false;

        try {
            File e = new File(context.getFilesDir() + context.getPackageName() + "/databases/" + dbName);
            if(!e.exists()) {
                if(!e.getParentFile().exists()) {
                    e.getParentFile().mkdirs();
                }

                e.createNewFile();
                is = context.getResources().openRawResource(rawRes);
                fos = new FileOutputStream(e);
                byte[] buffer = new byte[bufferSize];

                int count;
                while((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.flush();
            }

            flag = true;
        } catch (Exception var22) {
            var22.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (Exception var21) {
                    var21.printStackTrace();
                }
            }

            if(is != null) {
                try {
                    is.close();
                } catch (Exception var20) {
                    var20.printStackTrace();
                }
            }

        }

        return flag;
    }

    public static void forceStopApk(String pkgName) {
        Process sh = null;
        DataOutputStream os = null;

        try {
            sh = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(sh.getOutputStream());
            String e = "am force-stop " + pkgName + "\n";
            os.writeBytes(e);
            os.flush();
            sh.waitFor();
        } catch (InterruptedException | IOException var12) {
            var12.printStackTrace();
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context == null?Resources.getSystem():context.getResources();
        return resources.getDisplayMetrics();
    }

    public static void showSoftInput(Context context) {
        InputMethodManager input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        input.toggleSoftInput(0, 2);
    }

    public static void closeSoftInput(Context context) {
        InputMethodManager input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(input != null && ((Activity)context).getCurrentFocus() != null) {
            input.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;

        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return info;
    }

    public static String getPackage(Context context) {
        try {
            PackageInfo e = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return e.packageName;
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(Context context) {
        ArrayList list = null;

        try {
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            return am.getRunningAppProcesses();

        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return list;
    }

    public static ApplicationInfo getApplicationInfo(Context context, String processName) {
        if(processName == null) {
            return null;
        } else {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            List<ApplicationInfo> appList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            Iterator<ApplicationInfo> var4 = appList.iterator();

            ApplicationInfo appInfo;
            do {
                if(!var4.hasNext()) {
                    return null;
                }

                appInfo = var4.next();
            } while(!processName.equals(appInfo.processName));

            return appInfo;
        }
    }

    public static void killProcesses(Context context, int pid, String processName) {
        String cmd = "kill -9" + pid;
        String Command = "am force-stop " + processName + "\n";
        Process sh = null;

        try {
            sh = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(sh.getOutputStream());
            os.writeBytes(Command + "\n");
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        try {
            if(sh != null) {
                sh.waitFor();
            }
        } catch (InterruptedException var11) {
            var11.printStackTrace();
        }

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = processName.contains(":")?processName.split(":")[0]:processName;

        try {
            am.killBackgroundProcesses(packageName);
            Method e = am.getClass().getDeclaredMethod("forceStopPackage", new Class[]{String.class});
            e.setAccessible(true);
            e.invoke(am, new Object[]{packageName});
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }


    public static String runCommand(String[] command, String workDirectory) {
        String result = "";

        try {
            ProcessBuilder e = new ProcessBuilder(command);
            if(workDirectory != null) {
                e.directory(new File(workDirectory));
            }

            e.redirectErrorStream(true);
            Process process = e.start();
            InputStream in = process.getInputStream();

            String str;
            for(byte[] buffer = new byte[1024]; in.read(buffer) != -1; result = result + str) {
                str = new String(buffer);
            }

            in.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return result;
    }

    public static String runScript(String script) {
        try {
            final Process e = Runtime.getRuntime().exec(script);
            final StringBuilder builder = new StringBuilder();
            Thread tout = new Thread(new Runnable() {
                public void run() {
                    BufferedReader br = new BufferedReader(new InputStreamReader(e.getInputStream()), 8192);

                    try {
                        String line;
                        try {
                            while((line = br.readLine()) != null) {
                                builder.append(line).append("\n");
                            }
                        } catch (IOException var12) {
                            var12.printStackTrace();
                        }
                    } finally {
                        try {
                            br.close();
                        } catch (IOException var11) {
                            var11.printStackTrace();
                        }

                    }

                }
            });
            tout.start();
            final StringBuilder sberr = new StringBuilder();
            Thread terr = new Thread(new Runnable() {
                public void run() {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(e.getErrorStream()), 8192);

                    try {
                        String line;
                        try {
                            while((line = bufferedReader.readLine()) != null) {
                                sberr.append(line).append("\n");
                            }
                        } catch (IOException var12) {
                            var12.printStackTrace();
                        }
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException var11) {
                            var11.printStackTrace();
                        }

                    }

                }
            });
            terr.start();

            while(tout.isAlive()) {
                SystemClock.sleep(50L);
            }

            if(terr.isAlive()) {
                terr.interrupt();
            }

            String stdout = builder.toString();
            String stderr = sberr.toString();
            String result = stdout + stderr;
            return result;
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static boolean getRootPermission(Context context) {
        String packageCodePath = context.getPackageCodePath();
        Process process = null;
        DataOutputStream os = null;

        boolean var5;
        try {
            String e = "chmod 777 " + packageCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(e + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return true;
        } catch (Exception var15) {
            var5 = false;
        } finally {
            try {
                if(os != null) {
                    os.close();
                }

                if(process != null) {
                    process.destroy();
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

        }

        return var5;
    }

    public static List<String[]> getProcessRunningInfo() {
        List processList = null;

        try {
            String e = runCommandTopN1();
            processList = parseProcessRunningInfo(e);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return processList;
    }

    public static String runCommandTopN1() {
        String result = null;

        try {
            String[] e = new String[]{"/system/bin/top", "-n", "1"};
            result = runCommand(e, "/system/bin/");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return result;
    }

    public static List<String[]> parseProcessRunningInfo(String info) {
        ArrayList processList = new ArrayList();
        byte Length_ProcStat = 10;
        boolean bIsProcInfo = false;
        String[] rows = info.split("[\n]+");
        String[] var7 = rows;
        int var8 = rows.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            String row = var7[var9];
            if(!row.contains("PID")) {
                if(bIsProcInfo) {
                    String tempString = row.trim();
                    String[] columns = tempString.split("[ ]+");
                    if(columns.length == Length_ProcStat && !columns[9].startsWith("/system/bin/")) {
                        processList.add(columns);
                    }
                }
            } else {
                bIsProcInfo = true;
            }
        }

        return processList;
    }

    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static long getTotalMemory(Context context) {
        long memory = 0L;

        try {
            FileReader e = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(e, 8192);
            String memInfo = bufferedReader.readLine();
            String[] strs = memInfo.split("\\s+");
            String[] var7 = strs;
            int var8 = strs.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String str = var7[var9];
            }

            memory = (long)(Integer.valueOf(strs[1]).intValue() * 1024);
            bufferedReader.close();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return memory;
    }

}
