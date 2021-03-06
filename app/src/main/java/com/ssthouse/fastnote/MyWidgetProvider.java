package com.ssthouse.fastnote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by ssthouse on 2015/12/9.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    /**
     * 部件是有自己定义的更新的时间间隔的----这是定时调用的一个方法
     *
     * @param context          上下文
     * @param appWidgetManager widget manager
     * @param appWidgetIds     TODO id?? 是什么
     */
    @Override //更新部件时调用，在第1次添加部件时也会调用
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            rv.setOnClickPendingIntent(R.id.id_ll_main, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, rv);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (null == intent.getAction()) {
            return;
        }
        if (!intent.getAction().equalsIgnoreCase("com.ssthouse.update")
                && !intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            return;
        }
        ComponentName componentName = new ComponentName(context, MyWidgetProvider.class);
        String content = PreferenceHelper.getInstance(context).getNote();
        int[] widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
        int num = widgetIds.length;
        for (int i = 0; i < num; i++) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            remoteViews.setTextViewText(R.id.id_tv_main, content);
            remoteViews.setTextColor(R.id.id_tv_main,PreferenceHelper.getInstance(context).getColor());
            remoteViews.setOnClickPendingIntent(R.id.id_ll_main,
                    PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class),
                            PendingIntent.FLAG_CANCEL_CURRENT));
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
        //Log.e(TAG, "我收到了消息!");
    }
}
