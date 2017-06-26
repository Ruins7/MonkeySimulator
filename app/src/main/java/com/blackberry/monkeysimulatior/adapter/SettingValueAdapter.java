package com.blackberry.monkeysimulatior.adapter;import android.app.AlertDialog;import android.content.Context;import android.content.DialogInterface;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.EditText;import android.widget.TextView;import com.blackberry.monkeysimulatior.R;import com.blackberry.monkeysimulatior.util.MonkeySettings;import java.lang.reflect.Field;import java.util.List;/** * Created by frlee on 5/30/17. */public class SettingValueAdapter extends ArrayAdapter<String> {    private final MonkeySettings monkeySettingsObj = new MonkeySettings();    public MonkeySettings getMonkeySettingsObj() {        return monkeySettingsObj;    }    public SettingValueAdapter(Context context, int resource, List<String> objects) {        super(context, resource, objects);    }    @Override    public View getView(int position, View convertView, final ViewGroup parent) {        Object settingsNameObj = getItem(position);        final View oneParaView = LayoutInflater.from(getContext()).inflate(R.layout.settings_list, parent, false);        final TextView settingsName = (TextView) oneParaView.findViewById(R.id.setting_name_field);        final String settingName = settingsNameObj.toString();        settingsName.setText(settingName);        settingsName.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                // create a new EditText object inside the onClickListener each time and use setView()                final EditText settingsValue = new EditText(oneParaView.getContext());                // set up a Dialog                AlertDialog.Builder alDia = new AlertDialog.Builder(getContext());                alDia.setView(settingsValue);                alDia.setTitle("Entry value for " + settingName);                //alDia.setIcon(android.settingsValue.drawable.ic_dialog_info);                alDia.setPositiveButton("OK", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialog, int which) {                        for (String ss: monkeySettingsObj.getAllMonkeySettingsName()){                            if(settingName.equalsIgnoreCase(ss)){                                try{                                    Field field = monkeySettingsObj.getClass().getDeclaredField(settingName);                                    field.setAccessible(true);                                    field.set(monkeySettingsObj, settingsValue.getText().toString());                                } catch (NoSuchFieldException e) {                                    Log.e("ERROR TAG", "No such field for monkeySettingsObj");                                } catch (IllegalAccessException e) {                                    Log.e("ERROR TAG", "Illegal access field for monkeySettingsObj");                                }                            }                        }                        settingsName.setText(settingName+" : "+settingsValue.getText());                    }                });                alDia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialog, int which) {                        // do nothing                    }                });                alDia.show();            }        });        return oneParaView;    }}