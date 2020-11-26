package com.rabtman.acgcomic.mvp.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.rabtman.acgcomic.R
import com.rabtman.acgcomic.R2
import com.rabtman.acgcomic.base.constant.IntentConstant
import com.rabtman.acgcomic.base.constant.SystemConstant
import com.rabtman.acgcomic.mvp.model.dao.ComicDAO
import com.rabtman.acgcomic.mvp.model.entity.db.ComicCache
import com.rabtman.acgcomic.mvp.model.entity.jsoup.QiMiaoComicItem
import com.rabtman.acgcomic.mvp.ui.adapter.ComicCollectionAdapter
import com.rabtman.common.base.SimpleFragment
import com.rabtman.common.base.widget.loadsir.EmptyCollectionCallback
import com.rabtman.common.utils.RxUtil
import com.rabtman.router.RouterConstants
import com.rabtman.router.RouterUtils
import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.ResourceSubscriber

/**
 * @author Rabtman
 */
@Route(path = RouterConstants.PATH_COMIC_COLLECTION)
class ComicCollectionFragment : SimpleFragment() {

    @BindView(R2.id.rcv_comic_collection)
    internal lateinit var rcvQiMiaoComicItem: RecyclerView
    private lateinit var mAdapter: ComicCollectionAdapter
    private var mDisposable: Disposable? = null

    override fun getLayoutId(): Int {
        return R.layout.acgcomic_fragment_comic_collection
    }

    override fun initData() {
        mAdapter = ComicCollectionAdapter()
        rcvQiMiaoComicItem.layoutManager = GridLayoutManager(context, 3)
        rcvQiMiaoComicItem.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as ComicCache?
            RouterUtils.getInstance()
                    .build(RouterConstants.PATH_COMIC_QIMIAO_DETAIL)
                    .withParcelable(
                            IntentConstant.QIMIAO_COMIC_ITEM,
                            Gson().fromJson(item?.comicDetailJson, QiMiaoComicItem::class.java)
                    )
                    .navigation()
        }
        getComicItems()
    }

    override fun onResume() {
        super.onResume()
        if (isInited && isVisibled) {
            getComicItems()
        }
    }

    override fun onPause() {
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
        super.onPause()
    }

    override fun useLoadSir(): Boolean {
        return true
    }

    override fun onPageRetry(v: View?) {
        getComicItems()
    }

    /**
     * 获取收藏的所有番剧信息并显示出来
     */
    private fun getComicItems() {
        val dao = ComicDAO(
                mAppComponent
                        .repositoryManager()
                        .obtainRealmConfig(SystemConstant.DB_NAME)
        )
        mDisposable = dao.getComicCollectCaches()
                .compose(RxUtil.rxSchedulerHelper<List<ComicCache>>())
                .subscribeWith(object : ResourceSubscriber<List<ComicCache>>() {
                    override fun onNext(comicCacheList: List<ComicCache>) {
                        mAdapter.setList(comicCacheList)
                        if (comicCacheList.isNotEmpty()) {
                            showPageContent()
                        } else {
                            showPageEmpty()
                        }
                    }

                    override fun onError(t: Throwable) {
                        showError(R.string.msg_error_data_null)
                        showPageError()
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun showPageEmpty() {
        mLoadService?.showCallback(EmptyCollectionCallback::class.java)
    }
}
