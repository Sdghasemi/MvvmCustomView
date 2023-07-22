package com.hirno.rectangles.data

import com.hirno.rectangles.network.response.NetworkResponse

/**
 * A typealias used for using generic [ErrorResponseModel] as the ErrorModel type of [NetworkResponse]
 */
typealias GenericResponse<S> = NetworkResponse<S, ErrorResponseModel>