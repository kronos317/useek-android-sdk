package com.useek.library_kt_beta

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

/**
 * Created by Chris Lin on 19/4/2018.
 *
 * This singleton class provides the following features
 *
 *  - Set / Retrieve publisher id
 *  - Request server for the points of certain user based on game id and publisher id
 *
 */

class USeekManager {

    init {

    }

    private object Holder { val INSTANCE = USeekManager() }

    companion object {
        val sharedInstance: USeekManager by lazy { Holder.INSTANCE }
    }

    /**
     * Publisher id
     *
     * - Warning : You should set publisher id before loading video.
     *
     * You can set publisher id in Application.kt or Main Activity
     *
     *      ex : USeekManager.sharedInstance.publisherId = "\{your publisher id}"
     *
     */
    var publisherId: String? = null

    /**
     *
     * Queries the lastPlayPoints user has gained while playing the game.
     * The centralized server will return users's lastPlayPoints based on gameId and userId.
     *
     * - Precondition : Publisher ID should be set.
     *
     * @param gameId    unique game id provided by USeek (Not should be NULL)
     * @param userId    user's unique id registered in USeek (Optional)
     * @param complete  RequestPointsHandler object which will be triggered
     * after response is successfully retrieved.
     */
    fun requestPoints(gameId: String, userId: String?, complete: RequestPointsHandler) {

        // Check validate publisher id
        if (publisherId == null || publisherId!!.isEmpty()) {
            complete(null, null, Error("Not set publisher id"))
            return
        }

        // Check validate game id
        if (gameId.isEmpty()) {
            complete(null, null, Error("Invalid game id"))
            return
        }

        // Create parameter
        val params = HashMap<String, String>()
        if (userId != null && userId.isNotEmpty()) {
            params["external_user_id"] = userId
        } else {
            params["external_user_id"] = ""
        }

        // Create request url
        val urlString = "https://www.useek.com/sdk/1.0/$publisherId/$gameId/get_points"

        // execute request
        request(urlString, params, HttpMethod.GET) { response, error ->
            if (response != null) {
                val model = USeekPlaybackResultDataModel(response)
                complete(model.lastPlayPoints, model.totalPlayPoints, null)
            } else {
                complete(null, null, error)
            }
        }

    }

    /**
     * Make and execute http request with native android request methods.
     *
     * @param url           request url
     * @param params        parameters for request
     * @param method        private enum value for GET or POST
     * @param complete      callback for response of request
     */

    private fun request(url: String, params: HashMap<String, String>?, method: HttpMethod, complete: RequestCompleteHandler) {

        var reqParam = getParamsString(params, HttpMethod.POST)
        val mURL = URL(if (method == HttpMethod.POST) url else "$url?$reqParam")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = if (method == HttpMethod.POST) "POST" else "GET"

            if (method == HttpMethod.POST) {
                val wr = OutputStreamWriter(outputStream)
                wr.write(reqParam)
                wr.flush()
            }

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")

                if (responseCode in 200..299) {
                    complete(response.toString(), null)
                } else {
                    val error = Error(response.toString())
                    complete(null, error)
                }
            }
        }
    }

    private fun getParamsString(params: HashMap<String, String>?, methodType: HttpMethod): String? {
        try {
            if (params != null) {
                val result = StringBuilder()
                var isFirst = true
                for ((key, value) in params) {
                    if (isFirst) {
                        isFirst = false
                        if (methodType == HttpMethod.GET) {
                            result.append("?")
                        }
                    } else {
                        result.append("&")
                    }
                    result.append(URLEncoder.encode(key, "UTF-8"))
                    result.append("=")
                    result.append(URLEncoder.encode(value, "UTF-8"))
                }
                return result.toString()
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }

    }

    private enum class HttpMethod {
        GET, POST
    }

    private class USeekPlaybackResultDataModel {
        var publisherId: String? = null
            private set
        var gameId: String? = null
            private set
        var userId: String? = null
            private set
        var internalUserId: String? = null
            private set
        var finished: Boolean = false
            private set
        var lastPlayPoints: Int = 0
            private set
        var totalPlayPoints: Int = 0
            private set

        constructor(jsonString: String) {
            val json = JSONObject(jsonString)
            if (json.has("publisherId"))
                publisherId = json.getString("publisherId")
            if (json.has("gameId"))
                gameId = json.getString("gameId")
            if (json.has("externalUserId"))
                userId = json.getString("externalUserId")
            if (json.has("userId"))
                internalUserId = json.getString("userId")
            if (json.has("lastPlayPoints"))
                lastPlayPoints = json.getInt("lastPlayPoints")
            if (json.has("totalPoints"))
                totalPlayPoints = json.getInt("totalPoints")
            if (json.has("finished"))
                finished = json.getBoolean("finished")
        }

        constructor(json: JSONObject) {
            if (json.has("publisherId"))
                publisherId = json.getString("publisherId")
            if (json.has("gameId"))
                gameId = json.getString("gameId")
            if (json.has("externalUserId"))
                userId = json.getString("externalUserId")
            if (json.has("userId"))
                internalUserId = json.getString("userId")
            if (json.has("lastPlayPoints"))
                lastPlayPoints = json.getInt("lastPlayPoints")
            if (json.has("totalPoints"))
                totalPlayPoints = json.getInt("totalPoints")
            if (json.has("finished"))
                finished = json.getBoolean("finished")
        }

        constructor(data: HashMap<String, Any>) {
            publisherId = data["publisherId"] as? String
            gameId = data["gameId"] as? String
            userId = data["externalUserId"] as? String
            internalUserId = data["userId"] as? String
            lastPlayPoints = data["lastPlayPoints"] as? Int ?: 0
            totalPlayPoints = data["totalPoints"] as? Int ?: 0
            finished = data["finished"] as? Boolean ?: false
        }
    }
}