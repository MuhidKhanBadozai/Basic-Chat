package com.example.basics.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun UpdateChecker(context: Context) {
    var latestVersion by remember { mutableStateOf<String?>(null) }
    var downloadUrl by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val result = fetchLatestRelease()
        if (result != null) {
            val (version, url) = result
            val currentVersion =
                context.packageManager.getPackageInfo(context.packageName, 0).versionName

            // compare current app version with GitHub version
            if (version != currentVersion) {
                latestVersion = version
                downloadUrl = url
                showDialog = true
            }
        }
    }

    if (showDialog && latestVersion != null && downloadUrl != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Available") },
            text = { Text("A new version (${latestVersion}) is available. Would you like to update now?") },
            confirmButton = {
                TextButton(onClick = {
                    downloadApk(context, downloadUrl!!, "BasicChat_${latestVersion}.apk")
                    showDialog = false
                }) { Text("Update") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Later") }
            }
        )
    }
}

suspend fun fetchLatestRelease(): Pair<String, String>? = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://api.github.com/repos/MuhidKhanBadozai/Basic-Chat/releases/latest")
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val json = JSONObject(response)
        val tag = json.getString("tag_name").removePrefix("v")
        val assetUrl =
            json.getJSONArray("assets").getJSONObject(0).getString("browser_download_url")
        Pair(tag, assetUrl)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun downloadApk(context: Context, apkUrl: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(apkUrl))
        .setTitle("Downloading update")
        .setDescription("Downloading $fileName")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setMimeType("application/vnd.android.package-archive")

    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)

    // Open the Downloads app after enqueuing the download
    val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}
