package com.gordon.rrod.Sample.api;


import com.gordon.rrod.Sample.data.HttpResultEntity;
import com.gordon.rrod.Sample.data.LoginInfoEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Steven on 16/3/22.
 */
public interface RestApi {

    String SERVICE_URL = "http://www.jiu-zhang.com/";//默认外网
    String SERVICE_NAME = "九章云平台";
    String CONNECTOR = "BPMService/BPMServiceHandler.ashx";//BPMAsyncServiceHandler.ashx   BPMServiceHandler
    String LOGIN_CONNECTOR = "BPMService/BPMServiceHandler.ashx";//登陆的
    String PLATFORM = "android";

    /**
     * Action
     */

    /**
     * 登陆
     *
     * @param tenantName        租户名*
     * @param userName          用户名*
     * @param password          密码*
     * @param checkCode         验证码
     * @param secretProtectCode 动态验证码
     */
    @GET(LOGIN_CONNECTOR + "?action=logOn")
    Observable<String> login(
            @Query("tenantName") String tenantName,
            @Query("userName") String userName,
            @Query("password") String password,
            @Query("checkCode") String checkCode,
            @Query("secretProtectCode") String secretProtectCode);

    /**
     * 获取用户头像
     *
     * @param userId 用户ID
     */
    @GET(CONNECTOR + "?action=getUserHeadImage")
    Observable<HttpResultEntity<Object>> getUserHeadImage(
            @Query("userId") String userId);

    /**
     * 上传用户头像
     *
     * @param userId 用户ID
     */
    @GET(CONNECTOR + "?action=uploadUserHeadImage")
    Observable<HttpResultEntity<Object>> uploadUserHeadImage(
            @Query("userId") String userId);

//    /**
//     * 获取通用接口配置//HttpResultEntity<ClientConfig>
//     *
//     * @param configName 配置名
//     */
//    @GET(CONNECTOR + "?action=getClientConfiguration")
//    Observable<HttpResultEntity<ClientConfig>> getClientConfiguration(
//            @Query("configName") String configName);
//
//
//    /**
//     * 获取通用接口配置(实例数据主表子表中全部使用Caption-Value格式返回)
//     *
//     * @param configName 配置名
//     * @return
//     */
//    @GET(CONNECTOR + "?action=getFullClientConfigByNameInCaptionValue")
//    Observable<HttpResultEntity<ClientConfigByNameInCaptionValue>> getFullClientConfigByNameInCaptionValue(
//            @Query("configName") String configName);
//
//    /**
//     * 获取表单模板数据
//     *
//     * @param formTplId 模板ID
//     * @param version   本地缓存版本号
//     * @return
//     */
//    @GET(CONNECTOR + "?action=getFormTemplate")
//    Observable<HttpResultEntity<FormTplData>> getFormTplData(
//            @Query("formTplId") String formTplId,
//            @Query("version") int version
//    );
//
//    /**
//     * 附件资源 获取图片ID
//     *
//     * @param formTplId  表单模板ID
//     * @param FieldName  表单控件名
//     * @param instanceId 实例ID
//     * @return
//     */
//    @GET(CONNECTOR + "?action=getFileAttachments")
//    Observable<HttpResultEntity<List<FileAttachment>>> getFileAttachments(
//            @Query("formTplId") String formTplId,
//            @Query("conditionField") String FieldName,
//            @Query("instanceId") String instanceId
//    );
//
//    /**
//     * 获取图片的路径
//     *
//     * @param formTplId 表单模板ID
//     * @param fileId    数据源返回的字符串
//     * @param p         图片大小 例如 p200
//     * @return
//     */
//    @GET(CONNECTOR + "?action=showImage")
//    Observable<Object> showImage(
//            @Query("formTplId") String formTplId,
//            @Query("fileId") String fileId,
//            @Query("p") String p//大小
//    );
}
