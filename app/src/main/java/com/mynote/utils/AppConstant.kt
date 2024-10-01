package com.project.app.utils

object AppConstant {


    //urls and paths
    const val URL = "https://gtu.versemeta.in/api/"
    const val API_VERSION = "v1/"
    const val BASE_URL = URL + API_VERSION
    const val DOWNLOAD_FOLDER_NAME = "GTU notifier"

    const val GET_IP_INFO_URL = "http://ip-api.com/json"


    //nav keys
    const val SESSION_BROADCAST = "session_broadcast"
    const val VERIFICATION_BROADCAST = "session_broadcast"
    const val DATA_TYPE = "data_type"
    const val VERIFICATION_TYPE = "data_type"

    //pref keys
    const val CONSTANT_AUTH = "constant_auth"
    const val TEXT_SIZE = "text_size"
    const val PUSH_NOTIFICATION = "push_notification"
    const val USER_DATA = "user_data"
    const val PREF_KEY = "vidLin_pref"
    const val IS_NOTIFICATION_ON = "is_notification_on"


    //image bottom sheet
    const val CAMERA_PERMISSION_REQUEST_CODE = 1000
    const val GALLERY_PERMISSION_REQUEST_CODE = 1001
    const val CAMERA_INTENT_REQUEST_CODE = 1003
    const val GALLERY_INTENT_REQUEST_CODE = 1004
    const val GALLERY_ANDROID_13_REQUEST_CODE = 13
    const val CAMERA_ANDROID_13_REQUEST_CODE = 14
    const val GOOGLE_REQUEST_CODE = 2001

    //notification
    const val CHANNEL_ID = "note_notification"
    const val CHANNEL_NAME = "gtu"
    const val APP_UPDATE = "_app_update"
    const val OTHER = "_other"

    // not type
    const val NOTE_TYPE = "note_type"

    // note data list
    const val NOTE_DATA = "note_data"
    const val NOTE_ID = "note_id"

    class Error {
        companion object {
            const val EMPTY_ERROR: Int = 1001
            const val INTERNET_ERROR: Int = 1002
            const val SERVER_ERROR: Int = 1003
            const val UNKNOWN_ERROR: Int = 5001
        }


    }
}