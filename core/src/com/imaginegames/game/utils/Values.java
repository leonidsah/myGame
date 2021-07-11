package com.imaginegames.game.utils;

public final class Values {
    public static float UIScale = 3f;
    public static float LoadingScreenUIScale = 2f;
    public static final String SKIN_PATH = "skins/commodore-65/uiskin.json";
    public static boolean STAGES_DEBUG = false;
    public static boolean DESKTOP_FULLSCREEN = false;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String VERSION = "0.2";
    public static final String WELCOME_TITLE = "-";
    public static String RUS_CHARS = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮёЁ";
    public static String ENG_CHARS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static String OTHER_CHARS = "1234567890!@#$%^&*()_-=+~`{}|/<>[],.:;'?";
    public static String DEFAULT_CHARS = RUS_CHARS + ENG_CHARS + OTHER_CHARS;

    // Game process constants
    public static final int PLAYER_PWIDTH = 32;
    public static final int PLAYER_PHEIGHT = 145;
    public static final float PLAYER_SCALE = 0.025f;
    public static final float PLAYER_WIDTH = PLAYER_PWIDTH * PLAYER_SCALE;
    public static final float PLAYER_HEIGHT = PLAYER_PHEIGHT * PLAYER_SCALE;
}
