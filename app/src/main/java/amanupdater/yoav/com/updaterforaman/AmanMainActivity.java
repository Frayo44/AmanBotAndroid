package amanupdater.yoav.com.updaterforaman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import amanupdater.yoav.com.updaterforaman.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import amanupdater.yoav.com.updaterforaman.Server.AmanServer;

/**
 * Created by Yoav on 8/28/2016.
 */
public class AmanMainActivity extends AppCompatActivity {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aman_main);

        Intent intent = getIntent();
        String sessionID = intent.getStringExtra("SessionId");
        String misparMoamad = intent.getStringExtra("MisparMoamad");
    //    Toast.makeText(this, "SessionId: " + sessionID, Toast.LENGTH_SHORT).show();
        TextView tvCourses = (TextView) findViewById(R.id.tvAmanCourses);
        TextView tvLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);


        String html = AmanServer.requestData(sessionID, misparMoamad);

        Document doc = Jsoup.parse(html);
        // For maslolim
        Element element = doc.getElementById("ctl00_ctl00_cphMain_CPHMainContent_divSection1");
        Elements coursesNames = element.getElementsByClass("CourseName");
        Elements coursesStatus = element.getElementsByClass("CourseStatus");
        Elements eventsNames = doc.getElementsByClass("EventName");
        Elements eventPlace = doc.getElementsByClass("EventPlace");
        Elements eventDate = doc.getElementsByClass("EventDate");

        Element lastUpdate = doc.getElementById("ctl00_ctl00_cphMain_CPHMainContent_lblLastUpdate");

        for(int i = 0; i < coursesNames.size(); i++)
        {
            tvCourses.setText(tvCourses.getText() + "\n" + "CourseName: " + coursesNames.get(i).text() +
                    "CourseStatus: " + coursesStatus.get(i).text() );
        }

        tvLastUpdate.setText("Last Update: " + lastUpdate.text() + '\n');

        SharedPreferences prefs = this.getSharedPreferences(Consts.TAG, 0);
        prefs.edit().putString(Consts.KEY_LASTUPDATE, lastUpdate.text()).apply();

        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children

        GroupItem item1 = new GroupItem();
        item1.title = "מה חדש?";
        item1.icon = R.drawable.question;
        ChildItem child2 = new ChildItem();
        child2.title = "תאריך עדכון אחרון";
        child2.hint = lastUpdate.text();
        item1.items.add(child2);
        items.add(item1);



        GroupItem item2 = new GroupItem();
        item2.title = "מסלולים";
        item2.icon = R.drawable.blackboard;
        for(int i = 1; i < coursesNames.size(); i++)
        {
            ChildItem child = new ChildItem();
            child.title = coursesNames.get(i).text();
            child.hint = coursesStatus.get(i).text();
            item2.items.add(child);
        }
        items.add(item2);

        GroupItem item3 = new GroupItem();
        item3.title = "אירועים";
        item3.icon = R.drawable.calendar;
        for(int i = 1; i < eventsNames.size(); i++)
        {
            ChildItem child = new ChildItem();
            child.title = eventsNames.get(i).text();
            child.hint = "התרחש ב - "  + eventPlace.get(i).text() + " בתאריך " + eventDate.get(i).text();
            item3.items.add(child);
        }
        items.add(item3);



        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        listView.setChildDivider(new ColorDrawable(android.R.color.transparent));


    }

    private static class GroupItem {
        String title;
        int icon;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
        ImageView icon;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onDestroy() {
      /*  Intent receiverIntent = new Intent(this, Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 123456789, receiverIntent, 0);

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), alarmManager.INTERVAL_DAY, sender); */
        super.onDestroy();
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.icon = (ImageView) convertView.findViewById(R.id.ivIcon);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.icon.setImageResource(item.icon);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }



}
