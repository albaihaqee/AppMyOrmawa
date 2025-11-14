package com.inovarka.myormawa.network;

public class ApiConfig {
    // Ganti IP sesuai server kamu
    // 10.0.2.2 untuk emulator, IP lokal kamu kalau pakai HP
    public static final String BASE_URL = "http://10.0.2.2/Web_MyOrmawa/API/";

    public static final String LOGIN_URL = BASE_URL + "login.php";
    public static final String REGISTER_URL = BASE_URL + "register.php";
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "change_password.php";
}
