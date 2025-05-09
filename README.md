To use bottom navigation menu with fragment, please don't forget to make the id in menu.xml and navigatio.xml 
same

menu.xml
```
 <item
        android:id="@+id/upcoming_event"
        android:title="@string/upcoming"
        android:icon="@drawable/event_upcoming_24px"/>
```
navigation.xml
```
 <fragment
        android:id="@+id/upcoming_event"
        android:name="com.sobodigital.zulbelajarandroid.ui.pages.UpcomingEventFragment"
        android:label="@string/upcoming_event"
        tools:layout="@layout/fragment_upcoming_event" >
    </fragment>
```

As you can see the id, android:id="@+id/upcoming_event" both of menu and navigation is same, if not same the error will appear!
