package com.example.exercise2;

import java.text.SimpleDateFormat;

public class BaseUtils {

    public static final SimpleDateFormat FORMAT_TIME_SONG = new SimpleDateFormat("mm:ss");

    public static final String KEY_LIST_SONGS = "listsong";

    public static final int REQUEST_PERMISSION_READ_EXTERNAL = 1;

    public static final String KEY_SONGS = "song";

    public static final String KEY_STATUS_SONGS = "statusSong";

    public static final String KEY_TIME_CURRENT_SONG = "timeCurrentSong";

    public static final int TAB_DEFAULT_HOME = 0;

    public static final int TAB_DEFAULT = 1;

    public static final int ICON_TAB_DEFAULT = R.drawable.icontab;

    public static final int ICON_TAB_SELECTD = R.drawable.icontab_selected;

    public static final String KEY_INTENT = "keyIntent";

    public static final int RESULT_CODE = 1807;

    public static final int REQUEST_CODE = 1996;

    public static final String SHARED_PREFERENCES_NAME = "sharedPreferenceHomeActivity";

    ///////////////////////////////////////////////////

    public static final String NAME_CHANEL = "Music";

    public static final String SONG_COMPLETED = "COMPLETED";

    public static final int ID_NOTICATION = 6996;

    public static final String CHANNEL_ID = "channelid";

    public static final String ACTION_CREATE_SONG = "createSong";

    public static final String ACTION_START_SONG = "startSong";

    public static final String ACTION_PAUSE_SONG = "pauseSong";

    public static final String ACTION_NEXT_SONG = "nextSong";

    public static final String ACTION_BACK_SONG = "backSong";

    public static final String EXTRA_ITEM_POSITION = "extraitemtPosition";

    public static final String VIEW_PAUSE = "VIEWPAUSE";

    public static final String VIEW_START = "VIEWSTART";

    public static final String CREATE_SONG = "createSong";

    public static final String START_SONG = "startSong";

    public static final String PAUSE_SONG = "pauseSong";

    public static final String NEXT_SONG = "nextSong";

    public static final String BACK_SONG = "backSong";

}
