/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.jetty.web;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;
import org.springframework.beans.factory.annotation.Value;
import sample.jetty.service.HelloWorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.asynchttpclient.Dsl.asyncHttpClient;

@Controller
public class SampleController {

    @Autowired
    private HelloWorldService helloWorldService;

    @GetMapping("/")
    @ResponseBody
    public String helloWorld() throws IOException, ExecutionException, InterruptedException {
        /*try(AsyncHttpClient asyncHttpClient = asyncHttpClient()) {
            asyncHttpClient
					.prepareGet("http://www.example.com/")
					.execute()
					.toCompletableFuture()
					.thenApply(Response::getResponseBody)
					.thenAccept(System.out::println)
					.join();
		}*/

        AsyncHttpClientConfig cf = new DefaultAsyncHttpClientConfig.Builder()
                .setUseOpenSsl(true).build();

        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(cf);

        RequestBuilder requestBuilder =
                new RequestBuilder("GET")
                        .setUrl("http://www.euregex.eu/euregex-registers/euregex-b2c-adressermittlung/5/")
                        .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .addHeader("Accept-Charset","utf-8")
                        .addHeader("Accept-Encoding","gzip,deflate")
                        .addHeader("Host", "www.euregex.eu")
                        .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36")
                        .setProxyServer(new ProxyServer.Builder("112.214.73.253", 80));
        CompletableFuture<Response> responseFuture = asyncHttpClient.executeRequest(requestBuilder).toCompletableFuture();

//        Future<Response> f =
//                asyncHttpClient.prepareGet("https://www.euregex.eu/euregex-registers/euregex-b2c-adressermittlung/5/").execute();
        Response r = responseFuture.get();
        return r.getResponseBody();
    }

}
