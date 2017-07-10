package com.tamic.widget.sample;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Created by LIUYONGKUI726 on 2017-07-10.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MyRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}