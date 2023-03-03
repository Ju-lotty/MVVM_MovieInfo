package com.example.movie_app.data.repository

// RUNNING, SUCCESS, FAILED 세 가지 가능한 값이 있는 "Status"라는 열거형 클래스 선언
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

// eum class Status 상속 받는 변수 status, String 유형의 변수 msg 두 가지 속성이 있는 "NetworkState"라는 클래스 선언.
class NetworkState(val status: Status, val msg: String) {

    //이 클래스에는 "NetworkState" 유형의 "LOADED", "LOADING" 및 "ERROR"의 세 가지 정적 속성을 초기화 하는 동반 개체
    companion object {
        private val LOADED: NetworkState
        private val LOADING: NetworkState
        private val ERROR: NetworkState
        //클래스 로드 시 자동 초기화 되는 변수들 네트워크 상태에 따라 status 상태, msg가 초기화
        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
        }
    }
}