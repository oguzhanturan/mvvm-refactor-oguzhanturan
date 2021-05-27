package school.cactus.succulentshop.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import school.cactus.succulentshop.R
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.detail.ProductDetailRepository.FetchProductDetailRequestCallback
import school.cactus.succulentshop.product.list.ProductListFragmentDirections

class ProductDetailViewModel(
    private val productId: Int,
    private val repository: ProductDetailRepository
) : BaseViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

    private val _relatedProduct = MutableLiveData<List<ProductItem>>()
    val relatedProducts: LiveData<List<ProductItem>> = _relatedProduct

    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val _relatedProductsTextVisibility = MutableLiveData<Boolean>()
    val relatedProductsTextVisibility: LiveData<Boolean> = _relatedProductsTextVisibility

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductDetailFragmentDirections.openProductDetailFragmentSelf(it.id)
        navigation.navigate(action)
    }

    init {
        fetchProducts()
        _progressBarVisibility.value = true
    }

    private fun fetchProducts() {
        repository.fetchProductDetail(productId, object : FetchProductDetailRequestCallback {
            override fun onSuccess(product: ProductItem) {
                _product.value = product
                _progressBarVisibility.value = false
            }

            override fun onTokenExpired() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.your_session_is_expired,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.log_in,
                        action = {
                            navigateToLogin()
                        }
                    )
                )
            }

            override fun onUnexpectedError() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.unexpected_error_occurred,
                    duration = Snackbar.LENGTH_LONG,
                )
            }

            override fun onFailure() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.check_your_connection,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.retry,
                        action = {
                            fetchProducts()
                        }
                    )
                )
            }
        })

        repository.fetchRelatedProduct(productId, object :
            ProductDetailRepository.FetchRelatedProductRequestCallback {
            override fun onSuccess(productList: List<ProductItem>) {
                _relatedProductsTextVisibility.value = productList.isNotEmpty()
                _relatedProduct.value = productList
            }

            override fun onTokenExpired() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.your_session_is_expired,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.log_in,
                        action = {
                            navigateToLogin()
                        }
                    )
                )
            }

            override fun onUnexpectedError() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.unexpected_error_occurred,
                    duration = Snackbar.LENGTH_LONG,
                )
            }

            override fun onFailure() {
                _snackbarState.value = SnackbarState(
                    errorRes = R.string.check_your_connection,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    action = SnackbarAction(
                        text = R.string.retry,
                        action = {
                            fetchProducts()
                        }
                    )
                )
            }
        })
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(directions)
    }
}