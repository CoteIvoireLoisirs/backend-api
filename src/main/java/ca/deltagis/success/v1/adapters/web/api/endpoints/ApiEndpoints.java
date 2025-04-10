package ca.deltagis.success.v1.adapters.web.api.endpoints;

public class ApiEndpoints {
    public static final String BASE_API = "/api";
    public static final String BASE_PUBLIC_ASSETS = "";

    // Test i18n
    public static final String I18N = BASE_API + "/hello";

    // AUTH ENDPOINTS
    public static final String AUTH = BASE_API + "/auth";
    public static final String REGISTER_USER = "/signup";
    public static final String REGISTER_USER_ENDPOINT = AUTH + REGISTER_USER;
    public static final String REGISTER_FIRST_USER = "/signup-first-user";
    public static final String REGISTER_FIRST_USER_ENDPOINT = AUTH + REGISTER_FIRST_USER;
    public static final String LOGIN_USER = "/signin";
    public static final String LOGIN_USER_ENDPOINT = AUTH + LOGIN_USER;
    public static final String INVITE_USER = "/invite";
    public static final String INVITE_USER_ENDPOINT = AUTH + INVITE_USER;
    public static final String FORGET_PASSWORD = "/forget-password";
    public static final String FORGET_PASSWORD_ENDPOINT = AUTH + FORGET_PASSWORD;
    public static final String VERIFY_RECOVERY_CODE = "/verify-recovery-code";
    public static final String VERIFY_RECOVERY_CODE_ENDPOINT = AUTH + VERIFY_RECOVERY_CODE;
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String RESET_PASSWORD_ENDPOINT = AUTH + RESET_PASSWORD;
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String REFRESH_TOKEN_ENDPOINT = AUTH + REFRESH_TOKEN;
    public static final String LOGOUT_USER = "/log-out";
    public static final String LOGOUT_USER_ENDPOINT = AUTH + LOGOUT_USER;

    // USER
    public static final String USERS = BASE_API + "/users";
    // public static final String USER = BASE_API + "/user";
    public static final String USER_DETAIL = "/get-user-profile";
    public static final String USER_PROFILE = USERS + "/profile";
    public static final String USER_PROFILE_MULTI = "/workspaces";
    // public static final  String CREATE_USER_FROM_WORKSPACE = "/from/{workspaceCode}";

    // SWAGGER ENDPOINTS
    public static final String SWAGGER_UI = "/swagger-ui.html";
    public static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    public static final String SWAGGER_V3_API_DOCS = "/v3/api-docs/**";

    // RIGHTS
    public static final String RIGHTS = BASE_API + "/rights";
    public static final String PROFILES = BASE_API + "/profiles";
    public static final String PROFILE_WITH_AUTHORIZATION = "/module/rights";
    public static final String PROFILE_WITH_AUTHORIZATION_SINGLE = PROFILE_WITH_AUTHORIZATION + "/{id}";
    public static final String MODULES = BASE_API + "/modules";

    // WORKSPACE
    public static final String WORKSPACES = BASE_API + "/workspaces";
    public static final String PUBLIC_WORKSPACE = "/public";
    public static final String PUBLIC_WORKSPACE_ENDPOINT = WORKSPACES + "/public";
    public static final String ACTIVATED_WORKSPACE = "/enable/{code}";
    public static final String DISABLE_WORKSPACE = "/disable/{code}";
    public static final String WORKSPACE_CHANGE_IMAGE = "/{code}/change-image";

    // LOGS ENDPOINTS
    public static final String LOGS = BASE_API + "/logs";

    //PROJECT
    public static final String PROJECTS = BASE_API + "/projects";
    public static final String CLOSE_PROJECT = "/close";
    public static final String ARCHIVE_PROJECT = "/archive";
    public static final String PROJECT_FROM_WORKSPACE = "/workspace/{workspaceCode}";
    public static final String PROJECT_FAVORITE = BASE_API + "/project/favorite";
    public static final String PROJECT_CHANGE_IMAGE = "/{code}/change-image";

    //FICHIER
    public static final String UPLOAD_FILE = "/file";
    public static final String GET_DOWNLOAD_FILE = "/{fileName:.+}";

    //IMAGE
    public static final String UPLOAD = BASE_API + "/upload";
    public static final String PUBLIC_ASSET_ENDPOINT = BASE_PUBLIC_ASSETS + "/assets";

    //EXERCICE
    public static final String EXERCICE = BASE_API + "/exercice";

    //PERIODE
    public static final String PERIODE = BASE_API + "/periode";

    //COMPANY
    public static final String COMPANY = BASE_API + "/company";
    public static final String GET_COMPANY = "/GET/{codeworkspace}";
    public static final String COMPANY_CHANGE_IMAGE = "/{code}/change-image";

    public static final String COMPANY_UPDATE = BASE_API + "/update/company";

    //COUNTRIES
    public static final String COUNTRIES = BASE_API + "/countries";

    //CURRENCY
    public static final String CURRENCY = BASE_API + "/currencies";

    //BILLETAGE
    public static final String BILLETAGE = BASE_API + "/billetage";

    //UNITED
    public static final String UNIT = BASE_API + "/unit";
}
