package top.infra.maven.extension.shared;

public abstract class Orders {

    public static final int CI_OPTION_FAST = 1;
    public static final int CI_OPTION_INFRA = CI_OPTION_FAST + 1;
    public static final int CI_OPTION_MAVEN_BUILD_EXTENSION = CI_OPTION_INFRA + 1;
    public static final int CI_OPTION_MAVEN = CI_OPTION_MAVEN_BUILD_EXTENSION + 1;
    public static final int CI_OPTION_DOCKER = CI_OPTION_MAVEN + 1;
    public static final int CI_OPTION_GPG = CI_OPTION_DOCKER + 1;
    public static final int CI_OPTION_MAVEN_BUILD_POM = CI_OPTION_GPG + 1;

    public static final int ORDER_INFO_PRINTER = 1;
    public static final int ORDER_SYSTEM_TO_USER_PROPERTIES = ORDER_INFO_PRINTER + 1;
    public static final int ORDER_GIT_PROPERTIES = ORDER_SYSTEM_TO_USER_PROPERTIES + 1;
    public static final int ORDER_CI_OPTION_CONTEXT_FACTORY = ORDER_GIT_PROPERTIES + 1;
    public static final int ORDER_INFRASTRUCTURE_ACTIVATOR = ORDER_CI_OPTION_CONTEXT_FACTORY + 1;
    public static final int ORDER_CI_OPTION_CONFIG_LOADER = ORDER_CI_OPTION_CONTEXT_FACTORY + 1; //

    public static final int ORDER_CI_OPTION_INIT = ORDER_CI_OPTION_CONFIG_LOADER + 1;
    public static final int ORDER_MAVEN_SETTINGS_LOCALREPOSITORY = ORDER_CI_OPTION_INIT + 1;
    public static final int EVENT_AWARE_ORDER_MAVEN_SETTINGS_FILES = ORDER_MAVEN_SETTINGS_LOCALREPOSITORY + 1;
    public static final int EVENT_AWARE_ORDER_MAVEN_SETTINGS_SERVERS = EVENT_AWARE_ORDER_MAVEN_SETTINGS_FILES + 1;
    public static final int EVENT_AWARE_ORDER_MODEL_RESOLVER = EVENT_AWARE_ORDER_MAVEN_SETTINGS_SERVERS + 1; //
    public static final int EVENT_AWARE_ORDER_GPG_KEY = EVENT_AWARE_ORDER_MODEL_RESOLVER + 1;
    public static final int EVENT_AWARE_ORDER_MAVEN_PROJECT_INFO = EVENT_AWARE_ORDER_GPG_KEY + 1; //
    public static final int EVENT_AWARE_ORDER_GOAL_EDITOR = EVENT_AWARE_ORDER_MAVEN_PROJECT_INFO + 1;
    public static final int ORDER_GIT_FLOW_SEMANTIC_VERSION = EVENT_AWARE_ORDER_GOAL_EDITOR + 1;
    public static final int EVENT_AWARE_ORDER_DOCKER = ORDER_GIT_FLOW_SEMANTIC_VERSION + 1; //

    public static final int ORDER_SETTINGS_SECURITY = EVENT_AWARE_ORDER_DOCKER + 1;

    private Orders() {
    }
}