package com.jasoseol.test.utils

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL


/**
 * Created by apposter on 16. 1. 19..
 */
object FileUtil {

    /**
     * 앱 내 디렉토리 가져오기
     */
    fun getInnerDir(context: Context, dirName: String): File {
        return context.getDir(dirName, Context.MODE_PRIVATE)
    }


    /**
     * 파일 쓰기
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    fun writeFile(path: String, fileName: String, body: String, onComplete: ((filePath: String?) -> Unit)? = null) {
        CoroutineScope(Dispatchers.Main).launch {
            val filePath: String? = withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                var filePath: String?

                val dir = File(path)
                if (!dir.exists()) dir.mkdirs()

                // 기존의 파일이 있는 경우 지운다.
                val file = File(dir, fileName)
                if (file.exists()) file.delete()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                var fos: FileOutputStream? = null
                var buw: BufferedWriter? = null
                try {
                    fos = FileOutputStream(file)
                    buw = BufferedWriter(OutputStreamWriter(fos, "UTF8"))
                    buw.write(body)

                    filePath = file.path
                } catch (e: IOException) {
                    e.printStackTrace()
                    filePath = null
                } finally {
                    try {
                        buw?.close()
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                return@withContext filePath
            }

            onComplete?.invoke(filePath)
        }
    }

    /**
     * 비트맵 파일로 쓰기
     */
    fun writeBitmap(path: String, fileName: String, bitmap: Bitmap): String? {
        var filePath: String?

        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()

        // 기존의 파일이 있는 경우 지운다.
        val file = File(dir, fileName)
        if (file.exists()) file.delete()
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            filePath = file.path
        } catch (e: Exception) {
            e.printStackTrace()
            filePath = null
        } finally {
            bitmap.recycle()

            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return filePath
    }

    /**
     * 파일 읽기
     */
    fun readFile(path: String, fileName: String): String? {
        val filePath = path + File.separator + fileName
        val file = File(filePath)
        return if (file.exists()) {
            try {
                val inputStream = FileInputStream(file)
                val byteArrayOutputStream = ByteArrayOutputStream()

                var i = inputStream.read()
                while (i != -1) {
                    byteArrayOutputStream.write(i)
                    i = inputStream.read()
                }
                inputStream.close()
                byteArrayOutputStream.toString()
            } catch (e: IOException) {
                null
            }
        } else {
            null
        }
    }

    /**
     * 파일 다운로드
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    fun downloadFile(dir: File, fileName: String, fileLink: String, onComplete: ((filePath: String?) -> Unit)?) {
        CoroutineScope(Dispatchers.Main).launch {
            val filePath: String? = withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                var filePath: String?

                if (!dir.exists()) dir.mkdirs()

                // 기존의 파일이 있는 경우 지운다.
                val file = File(dir, fileName)
                if (file.exists()) file.delete()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    val dataInputStream = DataInputStream(URL(fileLink).openStream())
                    val buffer = ByteArray(1024)
                    var length: Int
                    val fos = FileOutputStream(file)
                    while (dataInputStream.read(buffer).also { length = it } > 0) {
                        fos.write(buffer, 0, length)
                    }

                    filePath = file.path
                } catch (e: Exception) {
                    e.printStackTrace()
                    filePath = null
                }

                return@withContext filePath
            }

            onComplete?.invoke(filePath)
        }
    }

    /**
     * 선택한 폴더 하위의 모든 내용 지우기 및 파일 지우기
     */
    fun deleteRecursive(fileOrDirectory: File, onComplete: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                if (fileOrDirectory.isDirectory) {
                    fileOrDirectory.listFiles()?.forEach { child ->
                        deleteRecursive(child)
                    }
                }

                fileOrDirectory.delete()
            }

            onComplete?.invoke()
        }
    }
}
