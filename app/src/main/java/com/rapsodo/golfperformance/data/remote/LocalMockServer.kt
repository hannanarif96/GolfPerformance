package com.rapsodo.golfperformance.data.remote

import android.content.Context
import fi.iki.elonen.NanoHTTPD
import timber.log.Timber
import java.io.IOException

class LocalMockServer(private val context: Context, port: Int = 8080) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val params = session.parameters
        
        Timber.d("LocalMockServer: Serving URI: $uri with params: $params")

        return when {
            uri.contains("api.php") -> handleApiRequest(params)
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found")
        }
    }

    private fun handleApiRequest(params: Map<String, List<String>>): Response {
        val endpoint = params["endpoint"]?.firstOrNull()
        
        return when (endpoint) {
            "players" -> {
                val json = loadJsonFromAssets("local_backend/players.json")
                newFixedLengthResponse(Response.Status.OK, "application/json", json)
            }
            "shots" -> {
                val playerId = params["playerId"]?.firstOrNull()
                val page = params["page"]?.firstOrNull()?.toIntOrNull() ?: 1
                val limit = params["limit"]?.firstOrNull()?.toIntOrNull() ?: 10
                
                val allShotsJson = loadJsonFromAssets("local_backend/shots.json")
                val paginatedJson = paginateShots(allShotsJson, playerId, page, limit)
                
                newFixedLengthResponse(Response.Status.OK, "application/json", paginatedJson)
            }
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", "{\"error\": \"Endpoint not found\"}")
        }
    }

    private fun paginateShots(json: String, playerId: String?, page: Int, limit: Int): String {
        return try {
            val shots = mutableListOf<String>()
            val regex = Regex("\\{[^{}]+\\}")
            val matches = regex.findAll(json)
            
            for (match in matches) {
                val shotJson = match.value
                if (playerId == null || shotJson.contains("\"playerId\": \"$playerId\"") || shotJson.contains("\"playerId\":\"$playerId\"")) {
                    shots.add(shotJson)
                }
            }

            val start = (page - 1) * limit
            if (start >= shots.size) return "[]"
            
            val end = (start + limit).coerceAtMost(shots.size)
            val paginatedShots = shots.subList(start, end)
            
            "[${paginatedShots.joinToString(",")}]"
        } catch (e: Exception) {
            Timber.e(e, "Error paginating shots")
            "[]"
        }
    }

    private fun loadJsonFromAssets(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Timber.e(e, "Error loading asset: $fileName")
            "[]"
        }
    }
}
