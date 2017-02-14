package com.littlejie.demo.modules.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlejie.demo.R;
import com.littlejie.demo.SharePrefsManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by littlejie on 2017/2/13.
 */
// TODO: 2017/2/13 优化获取 APP 图标、名称的速度
public class PackageAdapter extends BaseAdapter {

    private Context mContext;
    @SuppressWarnings("unchecked")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPackageInfoList = (List<MyPackageInfo>) msg.obj;
            notifyDataSetChanged();
        }
    };

    //对应包是否勾选 key:value = 包名:勾选
    private Map<String, Boolean> mMap;
    private List<MyPackageInfo> mPackageInfoList;
    private PackageInfoCacheRunnable mRunnable;
    private Drawable mDefaultDrawable;

    public PackageAdapter(Context context) {
        mContext = context;
        mDefaultDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
        mMap = SharePrefsManager.getInstance().getPackageSelect();
        List<PackageInfo> packageInfoList = getPackageInfos();
        //直接粗暴加载，效率低
        //去掉loadIcon()、loadLabel()提高效率，结合线程使用，数据多的情况可以减少1~2s
        mPackageInfoList = convert(packageInfoList);
        notifyDataSetChanged();
        //在子线程中加载数据，但需要处理数据变化导致crash问题
        //所以先通过前一步，获取到List的真正长度
        mRunnable = new PackageInfoCacheRunnable(packageInfoList);
        new Thread(mRunnable).start();
    }

    private List<PackageInfo> getPackageInfos() {
        return mContext.getPackageManager().getInstalledPackages(0);
    }

    @Override
    public int getCount() {
        return mPackageInfoList == null ? 0 : mPackageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPackageInfoList == null ? null : mPackageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_package, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        final MyPackageInfo pInfo = mPackageInfoList.get(position);
        //可能app没有图标
        vh.icon.setImageDrawable(pInfo.getIcon() == null ? mDefaultDrawable : pInfo.getIcon());
        vh.name.setText(pInfo.getName() + " | " + pInfo.getVersionName());
        vh.info.setText(pInfo.getPackageName());
        Boolean checked = mMap.get(pInfo.getPackageName());
        vh.checkBox.setChecked(checked == null ? false : checked);
        setChecked(vh.checkBox, pInfo.getPackageName());
        return convertView;
    }

    private void setChecked(final CheckBox checkBox, final String packageName) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.put(packageName, checkBox.isChecked());
                SharePrefsManager.getInstance().setPackageSelect(mMap);
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView icon;
        @BindView(R.id.tv_app_name)
        TextView name;
        @BindView(R.id.tv_app_info)
        TextView info;
        @BindView(R.id.cbx)
        CheckBox checkBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private List<MyPackageInfo> convert(List<PackageInfo> packageInfos) {
        List<MyPackageInfo> temps = new ArrayList<>();
        for (PackageInfo info : packageInfos) {
            MyPackageInfo temp = new MyPackageInfo();
            temp.setPackageName(info.packageName);
            temp.setVersionName(info.versionName);
            temps.add(temp);
        }
        return temps;
    }

    private class PackageInfoCacheRunnable implements Runnable {

        private List<PackageInfo> mPackageInfos;

        public PackageInfoCacheRunnable(List<PackageInfo> packageInfos) {
            mPackageInfos = packageInfos;
        }

        @Override
        public void run() {
            PackageManager pm = mContext.getPackageManager();
            for (int i = 0; i < mPackageInfos.size(); i++) {
                //获取app图标和名称的操作略费时间
                PackageInfo info = mPackageInfos.get(i);
                mPackageInfoList.get(i).setIcon(info.applicationInfo.loadIcon(pm));
                mPackageInfoList.get(i).setName(info.applicationInfo.loadLabel(pm).toString());
                sendMessage(mPackageInfoList);
            }
        }

        private void sendMessage(List<MyPackageInfo> packageInfos) {
            Message message = Message.obtain();
            message.obj = packageInfos;
            mHandler.sendMessage(message);
        }
    }

    private class MyPackageInfo implements Serializable {
        private String name;
        private String versionName;
        private String packageName;
        private Drawable icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }
    }
}
