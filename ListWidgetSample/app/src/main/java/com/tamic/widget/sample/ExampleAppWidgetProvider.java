package com.tamic.widget.sample;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by LIUYONGKUI726 on 2017-07-10.
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ExampleAppWidgetProvider extends AppWidgetProvider {

    String clickAction = "com.tamic.WidgetProvider.onclick";
    int i = 0;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // 获取Widget的组件名
        ComponentName thisWidget = new ComponentName(context,
                ExampleAppWidgetProvider.class);

        // 创建一个RemoteView
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.my_widget_layout);

        // 把这个Widget绑定到RemoteViewsService
        Intent intent = new Intent(context, MyRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

        // 设置适配器
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);

        // 设置当显示的widget_list为空显示的View
        remoteViews.setEmptyView(R.id.widget_list, R.layout.none_data);

        // 点击列表触发事件
        Intent clickIntent = new Intent(context, ExampleAppWidgetProvider.class);
        // 设置Action，方便在onReceive中区别点击事件
        clickIntent.setAction(clickAction);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.widget_list,
                pendingIntentTemplate);

        // 刷新按钮
        final Intent refreshIntent = new Intent(context,
                ExampleAppWidgetProvider.class);
        refreshIntent.setAction("refresh");
        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
                context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button_refresh,
                refreshPendingIntent);

        // 更新Wdiget
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }

   /**
     * 接收Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (action.equals("refresh")) {
            // 刷新Widget
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context,ExampleAppWidgetProvider.class);

            MyRemoteViewsFactory.mList.add("音乐"+ i);

            // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                    R.id.widget_list);

        } else if (action.equals(clickAction)) {
            // 单击Wdiget中ListView的某一项会显示一个Toast提示。
            Toast.makeText(context, intent.getStringExtra("content"),
                    Toast.LENGTH_SHORT).show();
        }
        i++;
    }

}