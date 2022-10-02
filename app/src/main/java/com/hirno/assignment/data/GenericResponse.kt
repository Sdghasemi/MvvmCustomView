package com.hirno.assignment.data

import com.hirno.assignment.network.response.NetworkResponse

/**
 * A typealias used for using generic [ErrorResponseModel] as the ErrorModel type of [NetworkResponse]
 */
typealias GenericResponse<S> = NetworkResponse<S, ErrorResponseModel>