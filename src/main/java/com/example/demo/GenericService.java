package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public interface GenericService {

   <R,T> T requestPost(
            String url,
            R body,
            TypeReference<T> responseType
    ) throws Exception;

    <T> T requestGet(
            String url,
            Map<String,String> queryParameters,
            TypeReference<T> responseType
    ) throws Exception;

    <T extends Object> T requestDelete(
            String url,
            Map<String,String> queryParameters,
            TypeReference<T> responseType
    ) throws Exception;


    abstract class Abstract implements GenericService{
        protected final OkHttpClient client = new OkHttpClient();
        protected final ObjectMapper objectMapper = new ObjectMapper();

        <T extends Object> T handleResponse(Request request, TypeReference<T> responseType) throws Exception {
            Response response = client.newCall(request).execute();

            String responseBody = response.body().string();

            if (response.isSuccessful()) {

                if(!responseBody.isEmpty() && responseBody!=null) {
                    // Process the response data here
                    System.out.println("Response: " + responseBody);

                    return objectMapper.readValue(responseBody, responseType);
                } else new Object();
            } else {

                // Process the response data here
                System.err.println("Request failed with code " + response.code()+" " + responseBody);

                ErrorDto responseObj = objectMapper.readValue(responseBody, ErrorDto.class);
                // Handle non-successful response

                throw new HttpException(responseObj.getMessage());
            }
            return null;
        }

    }

    class Base extends Abstract {

        @Override
        public <R,T> T requestPost(String url, R body, TypeReference<T> responseType) throws Exception {


            String jsonInput = objectMapper.writeValueAsString(body);

            RequestBody requestBody = RequestBody
                    .create(MediaType.parse("application/json"), jsonInput);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
           return super.handleResponse(request,responseType);
        };

        @Override
        public <T> T requestGet(String url, Map<String, String> queryParameters, TypeReference<T> responseType) throws Exception {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

            queryParameters.forEach((s, s2) -> urlBuilder.addQueryParameter(s,s2));

            String urlWithParameters = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(urlWithParameters)
//                    .header("login", LoginStore.getInstance().getLogin())
                    .get()
                    .build();

            return super.handleResponse(request,responseType);
        }

        @Override
        public <T> T requestDelete(String url, Map<String, String> queryParameters, TypeReference<T> responseType) throws Exception {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

            queryParameters.forEach((s, s2) -> urlBuilder.addQueryParameter(s,s2));

            String urlWithParameters = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(urlWithParameters)
                    .delete()
                    .build();

            return super.handleResponse(request,responseType);
        }


    }
}
