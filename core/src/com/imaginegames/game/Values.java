package com.imaginegames.game;

public final class Values {
    // Version format by changes significance: major/minor/days of developing
    public static final String VERSION = "0.2.8"; // Next stage 3: interpolation, scene2d ui actions
    public static final String WELCOME_TITLE = "(PURE SHIT VERSION)";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String SKIN_PATH = "skins/commodore-65/uiskin.json";
    public static final String RUS_CHARS = "йцукенгшщзхъфывапролджэячсмитьбюЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮёЁ";
    public static final String ENG_CHARS = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String OTHER_CHARS = "1234567890!@#$%^&*()_-=+~`{}|/<>[],.:;'?";
    public static final String DEFAULT_CHARS = RUS_CHARS + ENG_CHARS + OTHER_CHARS;
    public static boolean fullscreenMode = false;
    public static boolean stagesDebug = false;
    public static boolean stayOnLoadingScreen = false;
    public static boolean skipMainMenuScreen = false;
    public static boolean logUsingChat = false;
    public static boolean logFPS = false;
    public static float mainMenuScreenUIScale = 3f;
    public static float loadingScreenUIScale = 2f;

    // Game process constants
    public static final int PLAYER_PWIDTH = 32;
    public static final int PLAYER_PHEIGHT = 145;
    public static final float PLAYER_SCALE = 0.025f;
    public static final float PLAYER_WIDTH = PLAYER_PWIDTH * PLAYER_SCALE;
    public static final float PLAYER_HEIGHT = PLAYER_PHEIGHT * PLAYER_SCALE;
}
