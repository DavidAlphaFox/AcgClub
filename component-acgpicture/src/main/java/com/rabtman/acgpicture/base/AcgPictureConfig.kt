package com.rabtman.acgpicture.base

import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import com.rabtman.acgpicture.api.AcgPictureService
import com.rabtman.acgpicture.api.cache.AcgPictureCacheService
import com.rabtman.acgpicture.base.constant.HtmlConstant
import com.rabtman.common.base.CommonApplicationLike.Lifecycle
import com.rabtman.common.di.module.GlobeConfigModule.Builder
import com.rabtman.common.integration.ConfigModule
import com.rabtman.common.integration.IRepositoryManager

/**
 * @author Rabtman
 */

class AcgPictureConfig : ConfigModule {

    override fun applyOptions(context: Context, builder: Builder) {
        builder.baseurl(HtmlConstant.ACG_PICTURE_URL)
    }

    override fun registerComponents(context: Context, repositoryManager: IRepositoryManager) {
        repositoryManager.injectRetrofitService(AcgPictureService::class.java)
        repositoryManager.injectCacheService(AcgPictureCacheService::class.java)
        /*repositoryManager.injectRealmConfigs(
                RealmConfiguration.Builder()
                        .name(SystemConstant.DB_NAME)
                        .schemaVersion(SystemConstant.DB_VERSION)
                        .modules(Realm.getDefaultModule(), AcgPictureRealmModule())
                        .build()
        )*/
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<Lifecycle>) {
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycles: MutableList<ActivityLifecycleCallbacks>
    ) {
    }
}
