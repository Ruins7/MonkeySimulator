package com.blackberry.monkeysimulator;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blackberry.monkeysimulator.adapter.SettingValueAdapter;
import com.blackberry.monkeysimulator.entity.MonkeySettings;
import com.blackberry.monkeysimulator.tools.AssembleMonkeyCommand;
import com.blackberry.monkeysimulator.tools.CommonTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MonkeySettingsActivity extends AppCompatActivity {

    private ListView list;
    private TextView appName;
    private ImageView imageView;
    private Button runMonkeyButton;
    private Button backToMainAppListButton;
    private SettingValueAdapter settingValueAdapter;
    private RunMonkeyCommand runMonkeyCommand;
    private BackToAppList backToAppList;
    private String finalMonkeyCommand;
    private Intent launchIntent;
    private Intent launchIntentCurrent;
    private ComponentName componentName;
    private CommonTools commonTools = new CommonTools();
    private MonkeySettings monkeySettings = new MonkeySettings();

    private String appNameIntent;
    private String appVersionIntent;
    private String report;
    private StringBuffer sbReport;
    private String nameAndVersionPass;
    private Bitmap appIconIntent;
    private Process pc;
    private BufferedReader bufInput;
    private BufferedReader bufError;
    private String packName;

    private String APP_NAME;
    private String APP_VERSION;
    private String APP_ICON;
    private String MONKEY_REPORT;
    private String RETURN_LINE;
    private String VERSION_LARGE;
    private String ROOT_DEVICE_WARNING;
    private String MONKEY_COMMAND;
    private String ERROR_MESSAGE;
    private String EXECUTION_COMPLETE;
    private String MONKEY_REPORT_ACTIVITY;
    private String APP_NAME_VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.blackberry.monkeysimulator.R.layout.activity_monkey_settings);

        APP_NAME = this.getString(R.string.app_name_pass);
        APP_VERSION = this.getString(R.string.app_version_pass);
        APP_ICON = this.getString(R.string.app_icon_pass);
        RETURN_LINE = this.getString(R.string.return_line);
        VERSION_LARGE = this.getString(R.string.app_version_large);
        ROOT_DEVICE_WARNING = this.getString(R.string.root_device_warning);
        MONKEY_COMMAND = this.getString(R.string.monkey_command);
        ERROR_MESSAGE = this.getString(R.string.error_message);
        EXECUTION_COMPLETE = this.getString(R.string.monkey_execution_completed);
        MONKEY_REPORT_ACTIVITY = this.getString(R.string.monkey_reportactivity);
        MONKEY_REPORT = this.getString(R.string.monkey_report);
        APP_NAME_VERSION = this.getString(R.string.app_name_version_pass);

        appNameIntent = getIntent().getStringExtra(APP_NAME);
        appVersionIntent = getIntent().getStringExtra(APP_VERSION);
        appIconIntent = getIntent().getParcelableExtra(APP_ICON);

        //get short name of App
        packName = appNameIntent.substring(15, appNameIntent.length());
        packName = packName.replaceFirst(packName.substring(0, 1), packName.substring(0, 1).toUpperCase());

        appName = (TextView) findViewById(R.id.app_name_field_setting);
        imageView = (ImageView) findViewById(R.id.app_icon_field_setting);
        nameAndVersionPass = packName + RETURN_LINE + VERSION_LARGE + appVersionIntent;
        appName.setText(nameAndVersionPass);
        imageView.setImageBitmap(appIconIntent);

        list = (ListView) findViewById(R.id.monkey_settings_listView);
        settingValueAdapter = new SettingValueAdapter(this, android.R.layout.simple_list_item_1, monkeySettings.getAllMonkeySettingsName());
        list.setAdapter(settingValueAdapter);

        // click to assemble Monkey Command and run
        runMonkeyButton = (Button) findViewById(R.id.run_monkey);
        runMonkeyCommand = new RunMonkeyCommand();
        runMonkeyButton.setOnClickListener(runMonkeyCommand);

        backToMainAppListButton = (Button) findViewById(R.id.back_monkey_setting);
        backToAppList = new BackToAppList();
        backToMainAppListButton.setOnClickListener(backToAppList);
        launchIntent = new Intent(this, MainActivity.class);

    }

    private class RunMonkeyCommand implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 1.Must be rooted first
            if (!CommonTools.isRooted()) {
                commonTools.alarmToast(getBaseContext(), ROOT_DEVICE_WARNING);
                return;
            }
            // 2. verify parameters input
            if (!commonTools.paraInputVerify(settingValueAdapter.getMonkeySettingsObj(), getBaseContext())) {
                return;
            }
            // 2.Assemble command
            finalMonkeyCommand = AssembleMonkeyCommand.assembleMonkeyCommand(appNameIntent, settingValueAdapter.getMonkeySettingsObj());
            Log.e("....", finalMonkeyCommand);
            // 3.Open target application
            launchIntent = getPackageManager().getLaunchIntentForPackage(appNameIntent);
            startActivity(launchIntent);
            // 4.Execute monkey command
            // TODO use side button to control
            try {
                pc = Runtime.getRuntime().exec(finalMonkeyCommand);
                // 5. record report
                bufInput = new BufferedReader(new InputStreamReader(pc.getInputStream()));
                bufError = new BufferedReader(new InputStreamReader(pc.getErrorStream()));
                report = new String();
                sbReport = new StringBuffer();
                sbReport.append(MONKEY_COMMAND + finalMonkeyCommand + RETURN_LINE + RETURN_LINE);
                while ((report = bufInput.readLine()) != null) {
                    Log.e("..reportInput..", report);
                    sbReport.append(report);
                    sbReport.append(RETURN_LINE);
                }

                while ((report = bufError.readLine()) != null) {
                    Log.e("..reportError..", report);
                    sbReport.append(ERROR_MESSAGE + RETURN_LINE);
                    sbReport.append(report);
                    sbReport.append(RETURN_LINE);
                }
            } catch (Exception e) {
                Log.e("....", "SOMETHING WRONG");
                e.printStackTrace();
            }
            // 6. save current para values to application session
            MonkeySettings.setMonkeySettings(settingValueAdapter.getMonkeySettingsObj());
            // 7. Back to MonkeySimulator, Show result
            commonTools.alarmToast(getBaseContext(), EXECUTION_COMPLETE);
            launchIntentCurrent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            componentName = new ComponentName(getPackageName(), MONKEY_REPORT_ACTIVITY);
            launchIntentCurrent.setComponent(componentName);
            launchIntentCurrent.putExtra(MONKEY_REPORT, sbReport.toString());
            launchIntentCurrent.putExtra(APP_NAME_VERSION, nameAndVersionPass);
            launchIntentCurrent.putExtra(APP_ICON, appIconIntent);
            startActivity(launchIntentCurrent);
        }
    }

    private class BackToAppList implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SettingValueAdapter.setMonkeySettingsObj(new MonkeySettings());
            startActivity(launchIntent);
        }
    }

}
