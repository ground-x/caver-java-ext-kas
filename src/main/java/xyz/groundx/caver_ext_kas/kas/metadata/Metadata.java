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
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiCallback;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiClient;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.ApiException;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.api.DataUploadApi;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadAssetRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadAssetResponse;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadMetadataRequest;
import xyz.groundx.caver_ext_kas.rest_client.io.swagger.client.api.metadata.model.UploadMetadataResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Map;

/**
 * Representing an wrapping class tha connects Metadata API.
 */
public class Metadata {

    /**
     * Metadata API rest-client object.
     */
    DataUploadApi dataUploadApi;

    /**
     * Klaytn network id.
     */
    String chainId;

    /**
     * The ApiClient for connecting with KAS.
     */
    ApiClient apiClient;

    /**
     * Creates an Metadata API instance
     * @param chainId A Klaytn network chain id.
     * @param metadataApiClient The Api client for connecting with KAS.
     */
    public Metadata(String chainId, ApiClient metadataApiClient) {
        setChainId(chainId);
        setApiClient(metadataApiClient);
    }

    /**
     * Setter function for dataUploadApi
     * @param dataUploadApi An rest-client related Metadata API.
     */
    public void setDataUploadApi(DataUploadApi dataUploadApi) {
        this.dataUploadApi = dataUploadApi;
    }

    /**
     * Getter function for dataUploadApi.
     * @return DataUploadApi
     */
    public DataUploadApi getDataUploadApi() {
        return dataUploadApi;
    }
    
    /**
     * Setter function for chain id
     * @param chainId The klaytn network chain id.
     */
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    /**
     * Getter function for chain id.
     * @return String
     */
    public String getChainId() {
        return chainId;
    }

    /**
     * Setter function for apiClient
     * @param apiClient The ApiClient for connecting with KAS.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
        setDataUploadApi(new DataUploadApi(apiClient));
    }

    /**
     * Getter function for apiClient
     * @return
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Uploads an asset. Supported file types include jpg, png, and gif. After uploading the asset file, it returns a public URI to access the asset.<br>
     * POST /v1/metadata/asset
     *
     * <pre>Example:
     * {@code
     * 
     * UploadAssetResponse res = caver.kas.metadata.uploadAsset(file);
     * }
     * </pre>
     *
     * @param file Addes a file with multipart/form-data. File number is limited to one, and file size is limited to 10MB. If the file size exceeds 10MB, you will get an invalid input error.
     * @return UploadAssetResponse
     * @throws ApiException
     * @throws FileNotFoundException
     */
    public UploadAssetResponse uploadAsset(File file) throws ApiException {
        return uploadAsset(file, null);
    }

    /**
     * Uploads an asset. Supported file types include jpg, png, and gif. After uploading the asset file, it returns a public URI to access the asset.<br>
     * POST /v1/metadata/asset
     *
     * <pre>Example:
     * {@code
     * 
     * UploadAssetResponse res = caver.kas.metadata.uploadAsset(file);
     * }
     * </pre>
     *
     * @param file Addes a file with multipart/form-data. File number is limited to one, and file size is limited to 10MB. If the file size exceeds 10MB, you will get an invalid input error.
     * @param krn If you want to set up storage, you have to provide the Storage KRN object in the header. KRN must start with "krn:"
     * @return UploadAssetResponse
     * @throws ApiException
     * @throws FileNotFoundException
     */
    public UploadAssetResponse uploadAsset(File file, String krn) throws ApiException {
        return getDataUploadApi().uploadAsset(getChainId(),file, krn);
    }

    /**
     * Uploads an asset. Supported file types include jpg, png, and gif. After uploading the asset file, it returns a public URI to access the asset.<br>
     * POST /v1/metadata/asset
     *
     * <pre>Example:
     * {@code
     *
     * UploadAssetResponse res = caver.kas.metadata.uploadAsset(file, callback);
     * }
     * </pre>
     *
     * @param file Operator address to send transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call uploadAssetAsync(File file, ApiCallback<UploadAssetResponse> callback) throws ApiException {
        return uploadAssetAsync(file, null ,callback);
    }

    /**
     * Uploads an asset. Supported file types include jpg, png, and gif. After uploading the asset file, it returns a public URI to access the asset.<br>
     * POST /v1/metadata/asset
     *
     * <pre>Example:
     * {@code
     *
     * UploadAssetResponse res = caver.kas.metadata.uploadAsset(file, callback);
     * }
     * </pre>
     *
     * @param file Operator address to send transaction.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call uploadAssetAsync(File file, String krn, ApiCallback<UploadAssetResponse> callback) throws ApiException {
        return getDataUploadApi().uploadAssetAsync(getChainId(), file, krn ,callback);
    }


    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata);
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public UploadMetadataResponse uploadMetadata(Map<String, Object> metadata) throws ApiException {
        return uploadMetadata(metadata, null);
    }

    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata ,filname);
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param filname A file name of your choice. File extension must be .json. If the file name is already taken you will get a duplicate key error.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public UploadMetadataResponse uploadMetadata(Map<String, Object> metadata, String filename) throws ApiException {
        return uploadMetadata(metadata,filename , null);
    }


    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata ,filname, krn );
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param filname A file name of your choice. File extension must be .json. If the file name is already taken you will get a duplicate key error.
     * @param krn If you want to set up storage, you have to provide the Storage KRN object in the header. KRN must start with "krn:" 
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public UploadMetadataResponse uploadMetadata(Map<String, Object> metadata, String filename , String krn) throws ApiException {
        UploadMetadataRequest body = new UploadMetadataRequest();
        body.setMetadata(metadata);
        body.setFilename(filename);

        return dataUploadApi.uploadMetadata(getChainId(),body, krn);
    }

    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata , callback);
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call uploadMetadataAsync(Map<String, Object> metadata, ApiCallback<UploadMetadataResponse> callback) throws ApiException {
        return uploadMetadataAsync(metadata, null , null, callback);
    }

    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata ,filname, callback);
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param filname A file name of your choice. File extension must be .json. If the file name is already taken you will get a duplicate key error.
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call uploadMetadataAsync(Map<String, Object> metadata, String filename, ApiCallback<UploadMetadataResponse> callback) throws ApiException {
        return uploadMetadataAsync(metadata, filename , null, callback);
    }

    

    /**
     * Uploads metadata. After uploading the metadata, it returns an externally accessible public URI for that metadata.<br>
     * POST /v1/metadata
     *
     * <pre>Example:
     * {@code
     * Map<String, Object> metadata = new Map<String, Object>();
     * metadata.put("name", "Puppy Heaven NFT");
     * metadata.put("description", "This is a sample description");
     * metadata.put("image" , "url");
     * 
     * UploadMetadataResponse res = caver.kas.metadata.uploadMetadata(metadata ,filname, krn , callback);
     * }
     * </pre>
     *
     * @param metadata JSON metadata
     * @param filname A file name of your choice. File extension must be .json. If the file name is already taken you will get a duplicate key error.
     * @param krn If you want to set up storage, you have to provide the Storage KRN object in the header. KRN must start with "krn:" 
     * @param callback The callback function to handle response.
     * @return Call
     * @throws ApiException
     */
    public Call uploadMetadataAsync(Map<String, Object> metadata, String filename , String krn, ApiCallback<UploadMetadataResponse> callback) throws ApiException {
        UploadMetadataRequest body = new UploadMetadataRequest();
        body.setMetadata(metadata);
        body.setFilename(filename);

        return  dataUploadApi.uploadMetadataAsync(getChainId(), body, krn, callback);
    }
}
