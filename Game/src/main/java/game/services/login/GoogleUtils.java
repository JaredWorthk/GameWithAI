package game.services.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import game.dto.GoogleDTO;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class GoogleUtils {

    public static final String GOOGLE_CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    public static final String GOOGLE_CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    public static final String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static final String GOOGLE_REDIRECT_URI = getRedirectUri();

    private static String getRedirectUri() {
        String appUrl = System.getenv("APP_URL");

        // Nếu không có biến APP_URL -> Đang chạy trên máy tính cá nhân (Local)
        if (appUrl == null || appUrl.isEmpty()) {
            return "http://localhost:8080/Game_war_exploded/login-google";
        }

        // Nếu có biến APP_URL -> Đang chạy trên Render
        // Lưu ý: Render thường deploy ở thư mục gốc, không có đoạn /Game_war_exploded
        return appUrl + "/login-google";
    }

    public static String getToken(final String code) throws IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", GOOGLE_CLIENT_ID)
                        .add("client_secret", GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", "authorization_code")
                        .build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").toString().replaceAll("\"", "");
    }

    public static GoogleDTO getUserInfo(final String accessToken) throws IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(response, GoogleDTO.class);
    }

    public static void main(String[] args) {

    }
}
