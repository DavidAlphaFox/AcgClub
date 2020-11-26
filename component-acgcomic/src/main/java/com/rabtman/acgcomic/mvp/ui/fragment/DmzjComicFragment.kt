package com.rabtman.acgcomic.mvp.ui.fragment

import android.view.View
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.rabtman.acgcomic.R
import com.rabtman.acgcomic.R2
import com.rabtman.acgcomic.di.DaggerDmzjComicComponent
import com.rabtman.acgcomic.di.DmzjComicModule
import com.rabtman.acgcomic.mvp.DmzjComicContract
import com.rabtman.acgcomic.mvp.model.entity.DmzjComicItem
import com.rabtman.acgcomic.mvp.presenter.DmzjComicPresenter
import com.rabtman.acgcomic.mvp.ui.adapter.ComicMenuAdapter
import com.rabtman.acgcomic.mvp.ui.adapter.DmzjComicItemAdpater
import com.rabtman.common.base.BaseFragment
import com.rabtman.common.base.widget.DropDownMenu
import com.rabtman.common.di.component.AppComponent


/**
 * @author Rabtman
 */
class DmzjComicFragment : BaseFragment<DmzjComicPresenter>(), DmzjComicContract.View {
    @BindView(R2.id.ddm_comic_menu)
    lateinit var mMenuComicMain: DropDownMenu
    private var mSortGroup: RadioGroup? = null
    private var mSwipeRefresh: SwipeRefreshLayout? = null
    private var mRcvComicMain: RecyclerView? = null
    private var mDmzjComicItemAdapter: DmzjComicItemAdpater? = null
    private val headers = listOf("题材", "读者群", "进度", "地域")

    //菜单选项
    private val topic = arrayListOf(
            "全部", "冒险", "欢乐向", "格斗",
            "科幻", "爱情", "竞技", "魔法",
            "校园", "悬疑", "恐怖", "生活亲情",
            "百合", "伪娘", "耽美", "后宫",
            "萌系", "治愈", "武侠", "职场",
            "奇幻", "节操", "轻小说", "搞笑")
    private val groups = arrayListOf("全部", "少年", "少女", "青年")
    private val status = arrayListOf("全部", "连载", "完结")
    private val area = arrayListOf("全部", "日本", "内地", "欧美", "港台", "韩国", "其他")
    private val sort = arrayListOf("人气", "更新")

    //菜单选项适配器
    private var topicAdapter: ComicMenuAdapter? = null
    private var groupAdapter: ComicMenuAdapter? = null
    private var statusAdapter: ComicMenuAdapter? = null
    private var areaAdapter: ComicMenuAdapter? = null
    private var sortAdapter: ComicMenuAdapter? = null

    //弹出菜单视图集
    private var popupViews: List<View>? = null

    override fun getLayoutId(): Int {
        return R.layout.acgcomic_view_comic_menu
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerDmzjComicComponent.builder()
                .appComponent(appComponent)
                .dmzjComicModule(DmzjComicModule(this))
                .build()
                .inject(this)
    }

    override fun initData() {
        initDropDownMenu()
        mMenuComicMain.setDropDownMenu(headers, popupViews!!, initContentView())

        mPresenter.getComicInfos()
    }

    //初始化菜单布局
    private fun initDropDownMenu() {
        //题材
        val topicView = RecyclerView(mContext)
        topicView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey200))
        topicAdapter = ComicMenuAdapter(topic).apply {
            this.setOnItemClickListener { adapter, _, position ->
                if (adapter is ComicMenuAdapter) {
                    adapter.setCheckItem(position)
                    mMenuComicMain.setTabText(if (position == 0) headers[0] else topic[position])
                    mMenuComicMain.closeMenu()
                    mPresenter.changeMenuSelected(0, position)
                }
            }
        }
        topicView.layoutManager = GridLayoutManager(mContext, 4)
        topicView.adapter = topicAdapter
        //读者群
        val groupView = RecyclerView(mContext)
        groupView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey200))
        groupAdapter = ComicMenuAdapter(groups).apply {
            this.setOnItemClickListener { adapter, _, position ->
                if (adapter is ComicMenuAdapter) {
                    adapter.setCheckItem(position)
                    mMenuComicMain.setTabText(if (position == 0) headers[1] else groups[position])
                    mMenuComicMain.closeMenu()
                    mPresenter.changeMenuSelected(1, position)
                }
            }
        }
        groupView.layoutManager = GridLayoutManager(mContext, 4)
        groupView.adapter = groupAdapter
        //进度
        val statusView = RecyclerView(mContext)
        statusView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey200))
        statusAdapter = ComicMenuAdapter(status).apply {
            this.setOnItemClickListener { adapter, _, position ->
                if (adapter is ComicMenuAdapter) {
                    adapter.setCheckItem(position)
                    mMenuComicMain.setTabText(if (position == 0) headers[2] else status[position])
                    mMenuComicMain.closeMenu()
                    mPresenter.changeMenuSelected(2, position)
                }
            }
        }
        statusView.layoutManager = GridLayoutManager(mContext, 4)
        statusView.adapter = statusAdapter
        //地域
        val areaView = RecyclerView(mContext)
        areaView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey200))
        areaAdapter = ComicMenuAdapter(area).apply {
            this.setOnItemClickListener { adapter, _, position ->
                if (adapter is ComicMenuAdapter) {
                    adapter.setCheckItem(position)
                    mMenuComicMain.setTabText(if (position == 0) headers[3] else area[position])
                    mMenuComicMain.closeMenu()
                    mPresenter.changeMenuSelected(3, position)
                }
            }
        }
        areaView.layoutManager = GridLayoutManager(mContext, 4)
        areaView.adapter = areaAdapter
        //排序
        /*val sortView = RecyclerView(mContext)
        sortView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey200))
        sortAdapter = ComicMenuAdapter(sort)
        sortAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            if (adapter is ComicMenuAdapter) {
                adapter.setCheckItem(position)
                mMenuComicMain.setTabText(if (position == 0) headers[4] else sort[position])
                mMenuComicMain.closeMenu()
            }
        }
        sortView.layoutManager = GridLayoutManager(mContext, 4)
        sortView.adapter = sortAdapter*/
        popupViews = listOf<View>(topicView, groupView, statusView, areaView)
    }

    //初始化内容布局
    private fun initContentView(): View {
        val contentView = layoutInflater.inflate(R.layout.acgcomic_view_dmzj_comic_content, null)
        mSortGroup = contentView.findViewById(R.id.rg_dmzj_comic)
        mSwipeRefresh = contentView.findViewById(R.id.swipe_refresh_dmzj_comic)
        mRcvComicMain = contentView.findViewById(R.id.rcv_dmzj_comic)
        mDmzjComicItemAdapter = DmzjComicItemAdpater()
        mRcvComicMain?.layoutManager = GridLayoutManager(mContext, 2)
        mRcvComicMain?.adapter = mDmzjComicItemAdapter

        mSwipeRefresh?.setOnRefreshListener({ mPresenter.getComicInfos() })
        setSwipeRefreshLayout(mSwipeRefresh)
        return contentView
    }

    override fun showComicInfo(comicInfos: List<DmzjComicItem>) {
        mDmzjComicItemAdapter?.setList(comicInfos)
    }

    override fun showMoreComicInfo(comicInfos: List<DmzjComicItem>, canLoadMore: Boolean) {
        mDmzjComicItemAdapter?.addData(comicInfos)
        mDmzjComicItemAdapter?.loadMoreModule?.loadMoreComplete()
        if (!canLoadMore) {
            mDmzjComicItemAdapter?.loadMoreModule?.loadMoreEnd()
        }
    }

    override fun onLoadMoreFail() {
        mDmzjComicItemAdapter?.loadMoreModule?.loadMoreFail()
    }

}