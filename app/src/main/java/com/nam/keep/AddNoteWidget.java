package com.nam.keep;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.widget.RemoteViews;

import com.nam.keep.ui.note.AddNoteActivity;

/**
 * Implementation of App Widget functionality.
 */
public class AddNoteWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        // Create a unique action string for each button
        String actionAdd = "com.nam.widget.ADD";
        String actionCheckBox = "com.nam.widget.CHECKBOX";
        String actionMic = "com.nam.widget.MIC";
        String actionImage = "com.nam.widget.IMAGE";
        String actionBrush = "com.nam.widget.BRUSH";

        // Create intents with unique actions
        Intent intentAdd = new Intent(context, AddNoteActivity.class);
        intentAdd.setAction(actionAdd);
        PendingIntent pendingIntentAdd = PendingIntent.getActivity(
                context,
                0,
                intentAdd,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent intentCheckBox = new Intent(context, AddNoteActivity.class);
        intentCheckBox.setAction(actionCheckBox);
        intentCheckBox.putExtra("check_box_shortcut", true);
        PendingIntent pendingIntentCheckBox = PendingIntent.getActivity(
                context,
                1,
                intentCheckBox,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent intentMic = new Intent(context, AddNoteActivity.class);
        intentMic.setAction(actionMic);
        intentMic.putExtra("mic_shortcut", true);
        PendingIntent pendingIntentMic = PendingIntent.getActivity(
                context,
                2,
                intentMic,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent intentImage = new Intent(context, AddNoteActivity.class);
        intentImage.setAction(actionImage);
        intentImage.putExtra("image_shortcut", true);
        PendingIntent pendingIntentImage = PendingIntent.getActivity(
                context,
                3,
                intentImage,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent intentBrush = new Intent(context, AddNoteActivity.class);
        intentBrush.setAction(actionBrush);
        intentBrush.putExtra("brush_shortcut", true);
        PendingIntent pendingIntentBrush = PendingIntent.getActivity(
                context,
                4,
                intentBrush,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Get the layout for the widget and attach an on-click listener to the button.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_note_widget);
        views.setOnClickPendingIntent(R.id.button_add_widget, pendingIntentAdd);
        views.setOnClickPendingIntent(R.id.button_add_brush_widget, pendingIntentBrush);
        views.setOnClickPendingIntent(R.id.button_add_mic_widget, pendingIntentMic);
        views.setOnClickPendingIntent(R.id.button_add_image_widget, pendingIntentImage);
        views.setOnClickPendingIntent(R.id.button_add_check_box_widget, pendingIntentCheckBox);

        // Tell the AppWidgetManager to perform an update on the current app widget.
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}