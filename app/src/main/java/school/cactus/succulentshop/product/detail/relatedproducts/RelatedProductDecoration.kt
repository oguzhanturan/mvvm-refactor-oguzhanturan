package school.cactus.succulentshop.product.detail.relatedproducts

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import school.cactus.succulentshop.R

class RelatedProductDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing =
            view.context.resources.getDimensionPixelSize(R.dimen.item_product_list_spacing)

        outRect.right = spacing
    }
}