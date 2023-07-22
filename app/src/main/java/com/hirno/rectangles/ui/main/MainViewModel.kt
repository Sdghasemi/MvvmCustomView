package com.hirno.rectangles.ui.main

import androidx.lifecycle.*
import com.hirno.rectangles.R
import com.hirno.rectangles.data.source.RectanglesRepository
import com.hirno.rectangles.model.rectangle.RectangleItemModel
import com.hirno.rectangles.model.rectangle.RectanglesResponseModel
import com.hirno.rectangles.network.response.NetworkResponse
import kotlinx.coroutines.launch

/**
 * The [MainFragment] view model. Stores and manipulates state of the fragment
 *
 * @property rectanglesRepository The repository instance used to manage rectangles
 */
class MainViewModel(
    private val rectanglesRepository: RectanglesRepository,
) : ViewModel() {

    private val _rectangles = MutableLiveData<RectanglesResponseModel>()
    val rectangles: LiveData<RectanglesResponseModel> = _rectangles

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isDataLoadingError = MutableLiveData<Boolean>()
    val isDataLoadingError: LiveData<Boolean> = _isDataLoadingError

    private val _errorText = MutableLiveData<Any?>()
    val errorText: LiveData<Any?> = _errorText

    /**
     * Stores the state of our Retrofit request so that no duplicate request is performed
     */
    private var isRequestingRectangles = false

    /**
     * A pending rectangles request waiting for the network state to perform
     */
    private var pendingRectanglesRequest: Runnable? = null

    /**
     * Requests rectangles from repository
     */
    fun loadRectangles() {
        if (isRequestingRectangles) return
        viewModelScope.launch {
            isRequestingRectangles = true
            _dataLoading.value = true
            when (val result = rectanglesRepository.getRectangles()) {
                is NetworkResponse.Success -> onRectanglesLoadingSuccessful(result.body)
                is NetworkResponse.ApiError -> onRectanglesLoadingFailed(error = result.body.title)
                is NetworkResponse.NetworkError -> onRectanglesLoadingFailed(error = R.string.failed_to_connect_to_remote_server, shouldRetry = true)
                is NetworkResponse.UnknownError -> onRectanglesLoadingFailed(error = R.string.an_error_occurred)
            }
            _dataLoading.value = false
            isRequestingRectangles = false
        }
    }

    private fun onRectanglesLoadingSuccessful(rectangles: RectanglesResponseModel) {
        _rectangles.value = rectangles
        _errorText.value = null
        _isDataLoadingError.value = false
        pendingRectanglesRequest = null
    }

    private fun onRectanglesLoadingFailed(error: Any?, shouldRetry: Boolean = false) {
        _errorText.value = error
        _isDataLoadingError.value = true
        pendingRectanglesRequest = if (shouldRetry) Runnable {
            loadRectangles()
        } else null
    }

    /**
     * Requests an update of a rectangle in cache
     */
    fun updateRectangle(rectangle: RectangleItemModel) {
        viewModelScope.launch {
            rectanglesRepository.updateRectangle(rectangle)
        }
    }

    /**
     * Performs the pending requests if there are any
     */
    fun onNetworkConnectivityChanged() {
        pendingRectanglesRequest?.run()
    }
}

/**
 * The factory of [MainViewModel]
 *
 * @property rectanglesRepository The rectangles repository
 * @constructor Creates a [MainViewModel] instance
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val rectanglesRepository: RectanglesRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MainViewModel(rectanglesRepository) as T)
}