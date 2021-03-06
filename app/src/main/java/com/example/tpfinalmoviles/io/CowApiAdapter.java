package com.example.tpfinalmoviles.io;

import com.example.tpfinalmoviles.Utils.ConfigServer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CowApiAdapter {

    private static CowApiService API_SERVICE;
    private static String baseUrl,ultimaIp = null;

    public static CowApiService getApiService() {
        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if ( (ConfigServer.url != null) && (baseUrl==null || !baseUrl.equals(ConfigServer.url)))
             baseUrl = ConfigServer.url + "api/";
        else
            baseUrl = " http://localhost:8080/api/";

        if (API_SERVICE == null || !ultimaIp.equals(baseUrl)) {
            ultimaIp = baseUrl;
            System.out.println("ENTRO " + baseUrl + "  -- " + ConfigServer.url);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- log level
                    .build();
            API_SERVICE = retrofit.create(CowApiService.class);
        }
        return API_SERVICE;
    }


}
