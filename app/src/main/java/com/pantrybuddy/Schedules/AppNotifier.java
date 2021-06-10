package com.pantrybuddy.Schedules;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pantrybuddy.server.Server;

public class AppNotifier extends Worker{
    public AppNotifier(@NonNull @org.jetbrains.annotations.NotNull Context context, @NonNull @org.jetbrains.annotations.NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Result doWork() {
       Server server = new Server(getApplicationContext());
       server.fetchExpiredProducts();
        return Result.success();
    }

}
