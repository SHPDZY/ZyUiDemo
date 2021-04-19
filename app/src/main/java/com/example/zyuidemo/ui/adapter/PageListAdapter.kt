package com.example.zyuidemo.ui.adapter

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.View
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.ScreenUtils
import com.example.zyuidemo.R
import com.example.zyuidemo.base.multitype.vu.BaseVu
import com.example.zyuidemo.databinding.LayoutPictureBinding
import com.example.zyuidemo.widget.photodraweeview.OnViewTapListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imageutils.BitmapUtil

/**
 *  预览图片adapter
 */
class PageListView : BaseVu<LayoutPictureBinding, PictureItem>(), View.OnClickListener,
    OnViewTapListener {

    private lateinit var mdata: PictureItem

    override fun getLayoutId() = R.layout.layout_picture

    override fun bindData(data: PictureItem) {
        mdata = data

        binding.picture?.let {
            it.isEnableDraweeMatrix = false
            val url = data.url
            it.tag = adapterPosition
            val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("$url?x-oss-process=image/resize,w_400/quality,Q_70"))
                    .setResizeOptions(ResizeOptions(ScreenUtils.getAppScreenWidth(), ScreenUtils.getAppScreenHeight(), BitmapUtil.MAX_BITMAP_SIZE * 10))
                    .build()
            val hierarchy = GenericDraweeHierarchyBuilder.newInstance(it.resources)
                    .setFailureImageScaleType(ScalingUtils.ScaleType.CENTER)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setFailureImage(R.drawable.picture_icon_data_error)
            ViewCompat.setTransitionName(it, url + adapterPosition)
            val controller = Fresco.newDraweeControllerBuilder()
            controller.imageRequest = request
            controller.lowResImageRequest = ImageRequest.fromUri("$url?x-oss-process=image/resize,w_50/quality,Q_30")
            controller.autoPlayAnimations = true
            controller.oldController = it.controller
            controller.controllerListener = object : BaseControllerListener<ImageInfo?>() {

                override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
                    it.isEnableDraweeMatrix = false
                    super.onIntermediateImageFailed(id, throwable)
                }

                override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                    it.isEnableDraweeMatrix = false
                    super.onIntermediateImageSet(id, imageInfo)
                }

                override fun onFailure(id: String?, throwable: Throwable?) {
                    it.isEnableDraweeMatrix = false
                    super.onFailure(id, throwable)
                }

                override fun onFinalImageSet(id: String, imageInfo: ImageInfo?, animatable: Animatable?) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    if (imageInfo == null) {
                        return
                    }
                    it.update(imageInfo.width, imageInfo.height)
                    it.isEnableDraweeMatrix = true
                }
            }
            it.maximumScale = 20f
            it.mediumScale = 5f
            it.hierarchy = hierarchy.build()
            it.controller = controller.build()
            it.onViewTapListener = this
        }
    }

    override fun onClick(v: View?) {
        mVuCallBack?.onCallBack(mdata, getAdapterPos())
    }

    override fun onViewTap(view: View?, x: Float, y: Float) {
        mVuCallBack?.onCallBack(mdata, getAdapterPos())
    }
}

data class PictureItem(
    val id: Int,
    val url: String
)