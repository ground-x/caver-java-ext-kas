/*
 * Copyright 2020 The caver-java-ext-kas Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.groundx.caver_ext_kas.kas.metadata;

import com.squareup.okhttp.Call;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import xyz.groundx.caver_ext_kas.CaverExtKAS;
import xyz.groundx.caver_ext_kas.Config;
import xyz.groundx.caver_ext_kas.kas.KAS;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadAssetResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadMetadataResponse;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class MetadataAPITest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    static CaverExtKAS caver;
    static String krn;
    static File file = new File("src/test/java/xyz/groundx/caver_ext_kas/fixture/img-jpg.jpg");
    static Map<String , Object> metadata;
    @BeforeClass
    public static void init() throws ApiException {
        Config.init();
        caver = Config.getCaver();
        caver.kas.metadata.getApiClient().setDebugging(true);
        krn = Config.getStorageKrn();
        metadata = new HashMap<String , Object>();
        metadata.put("name", "Puppy Heaven NFT");
        metadata.put("description", "This is a sample description");
        metadata.put("image", "https://metadata-store.klaytnapi.com/e2d83vdb-c108-823c-d5f3-69vdf2d871c51/4a85e6be-3215-93e6-d8a9-3a7d633584e7.png");
    }

    @Test
    public void enableAPITest() {
        assertNotNull(caver.kas.metadata);
    }

    @Test
    public void uploadAssetTest() throws ApiException {
        UploadAssetResponse res = caver.kas.metadata.uploadAsset(file);
        assertNotNull(res);
    }

    @Test
    public void uploadAssetWithKrnTest() throws ApiException {
        UploadAssetResponse res = caver.kas.metadata.uploadAsset(file , krn);
        assertNotNull(res);
    }

    @Test
    public void uploadAssetAsyncTest() throws ApiException, InterruptedException, ExecutionException {
        CompletableFuture<UploadAssetResponse> future = new CompletableFuture<>();
        Call res = caver.kas.metadata.uploadAssetAsync(file, new ApiCallback<UploadAssetResponse>() {
            @Override
            public void onSuccess(UploadAssetResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {               
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
            }
        });

        if (future.isCompletedExceptionally()){
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void uploadAssetAsyncWithKrnTest() throws ApiException, InterruptedException, ExecutionException {
        CompletableFuture<UploadAssetResponse> future = new CompletableFuture<>();
        Call res = caver.kas.metadata.uploadAssetAsync(file, krn ,new ApiCallback<UploadAssetResponse>() {
            @Override
            public void onSuccess(UploadAssetResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {               
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
            }
        });

        if (future.isCompletedExceptionally()){
            fail();
        } else {
            assertNotNull(future.get());
        }
    }

    @Test
    public void uploadMetadataTest() throws ApiException {
        UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata);
        assertNotNull(res);
    }

    @Test
    public void uploadMetadataWithFilenameTest() throws ApiException {
        String filename = "caver-test-" + new Date().getTime()+ ".json";
        UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata, filename);
        assertNotNull(res);
    }

    @Test
    public void uploadMetadataWithKrnTest() throws ApiException {
        UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata,null, krn);
        assertNotNull(res);
    }

    @Test
    public void uploadMetadataWithFullParamsTest() throws ApiException {
        String filename = "caver-test-" + new Date().getTime()+ ".json";
        UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata, filename, krn);
        assertNotNull(res);
    }
    
    @Test
    public void uploadMetadataAsyncTest() throws ApiException, InterruptedException, ExecutionException {
        CompletableFuture<UploadMetadataResponse> future = new CompletableFuture<>();
        Call result = caver.kas.metadata.uploadMetadataAsync(metadata, new ApiCallback<UploadMetadataResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(UploadMetadataResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
            }
           
        });

        if (future.isCompletedExceptionally()){
            fail();
        } else {
            assertNotNull(future.get());
        }
        assertNotNull(result);
    }

    @Test
    public void uploadMetadataAsyncWithFilenameTest() throws ApiException, InterruptedException, ExecutionException {
        CompletableFuture<UploadMetadataResponse> future = new CompletableFuture<>();
        String filename = "caver-test-" + new Date().getTime()+ ".json";
        Call result = caver.kas.metadata.uploadMetadataAsync(metadata, filename ,new ApiCallback<UploadMetadataResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(UploadMetadataResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
            }
           
        });

        if (future.isCompletedExceptionally()){
            fail();
        } else {
            assertNotNull(future.get());
        }
        assertNotNull(result);
    }

    @Test
    public void uploadMetadataAsyncWithKrnTest() throws ApiException, InterruptedException, ExecutionException {
        CompletableFuture<UploadMetadataResponse> future = new CompletableFuture<>();
        String filename = "caver-test-" + new Date().getTime()+ ".json";
        Call result = caver.kas.metadata.uploadMetadataAsync(metadata, filename , krn ,new ApiCallback<UploadMetadataResponse>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                future.completeExceptionally(e);
            }

            @Override
            public void onSuccess(UploadMetadataResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
                future.complete(result);
            }

            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
            }

            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
            }
           
        });

        if (future.isCompletedExceptionally()){
            fail();
        } else {
            assertNotNull(future.get());
        }
        assertNotNull(result);
    }

}