package com.netflix.eureka.registry;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.LookupService;
import com.netflix.discovery.shared.Pair;
import com.netflix.eureka.lease.LeaseManager;

import java.util.List;
import java.util.Map;

/**
 * @author Tomasz Bak
 * 应用实例注册表接口。
 * 它继承了 LookupService 、LeaseManager 接口，提供应用实例的注册与发现服务。另外，它结合实际业务场景，定义了更加丰富的接口方法。
 */
public interface InstanceRegistry extends LeaseManager<InstanceInfo>, LookupService<String> {
        // ====== 开启与关闭相关 ======
    void openForTraffic(ApplicationInfoManager applicationInfoManager, int count);

    void shutdown();

    @Deprecated
    void storeOverriddenStatusIfRequired(String id, InstanceStatus overriddenStatus);

    // ====== 应用实例状态变更相关 ======
    void storeOverriddenStatusIfRequired(String appName, String id, InstanceStatus overriddenStatus);

    boolean statusUpdate(String appName, String id, InstanceStatus newStatus,
                         String lastDirtyTimestamp, boolean isReplication);

    boolean deleteStatusOverride(String appName, String id, InstanceStatus newStatus,
                                 String lastDirtyTimestamp, boolean isReplication);

    Map<String, InstanceStatus> overriddenInstanceStatusesSnapshot();

    Applications getApplicationsFromLocalRegionOnly();

    List<Application> getSortedApplications();

    /**
     * Get application information.
     *
     * @param appName The name of the application
     * @param includeRemoteRegion true, if we need to include applications from remote regions
     *                            as indicated by the region {@link java.net.URL} by this property
     *                            {@link com.netflix.eureka.EurekaServerConfig#getRemoteRegionUrls()}, false otherwise
     * @return the application
     */
    Application getApplication(String appName, boolean includeRemoteRegion);

    /**
     * Gets the {@link InstanceInfo} information.
     *
     * @param appName the application name for which the information is requested.
     * @param id the unique identifier of the instance.
     * @return the information about the instance.
     */
    InstanceInfo getInstanceByAppAndId(String appName, String id);

    /**
     * Gets the {@link InstanceInfo} information.
     *
     * @param appName the application name for which the information is requested.
     * @param id the unique identifier of the instance.
     * @param includeRemoteRegions true, if we need to include applications from remote regions
     *                             as indicated by the region {@link java.net.URL} by this property
     *                             {@link com.netflix.eureka.EurekaServerConfig#getRemoteRegionUrls()}, false otherwise
     * @return the information about the instance.
     */
    InstanceInfo getInstanceByAppAndId(String appName, String id, boolean includeRemoteRegions);

    void clearRegistry();

    // ====== 响应缓存相关 ======
    void initializedResponseCache();

    ResponseCache getResponseCache();

    // ====== 自我保护模式相关 ======
    long getNumOfRenewsInLastMin();

    int getNumOfRenewsPerMinThreshold();

    int isBelowRenewThresold();

    List<Pair<Long, String>> getLastNRegisteredInstances();

    List<Pair<Long, String>> getLastNCanceledInstances();


    // ====== 调试/监控相关 ======
    /**
     * Checks whether lease expiration is enabled.
     * @return true if enabled
     */
    boolean isLeaseExpirationEnabled();

    boolean isSelfPreservationModeEnabled();

}
