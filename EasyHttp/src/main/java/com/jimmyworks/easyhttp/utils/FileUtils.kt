package com.jimmyworks.easyhttp.utils

import android.webkit.MimeTypeMap
import java.io.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat
import java.util.*


/**
 * 檔案處理共用
 *
 * @author Jimmy Kang
 */
class FileUtils {
    companion object {

        /**
         * 新增資料夾
         *
         * @param path 資料夾路徑
         */
        fun mkdir(path: String): Boolean {
            return mkdir(File(path))
        }

        /**
         * 新增資料夾
         *
         * @param file 資料夾
         */
        fun mkdir(file: File): Boolean {
            return if (!file.exists()) {
                file.mkdirs()
            } else false
        }

        /**
         * 刪除檔案
         *
         * @param path 檔案路徑
         * @return 是否刪除成功
         */
        fun delete(path: String): Boolean {
            return delete(File(path))
        }

        /**
         * 刪除檔案
         *
         * @param file 檔案
         * @return 是否刪除成功
         */
        fun delete(file: File): Boolean {
            if (!file.exists()) return false
            return if (file.isDirectory) false else file.delete()
        }

        /**
         * 刪除資料夾
         *
         * @param dir 資料夾
         * @return 是否刪除成功
         */
        fun deleteDir(dir: File): Boolean {
            if (!dir.exists()) return false
            if (!dir.isDirectory) return false

            val listFiles: Array<File> = dir.listFiles() ?: arrayOf()

            for (file in listFiles) {
                delete(file)
            }
            return dir.delete()
        }

        /**
         * 複製檔案
         *
         * @param from 來源
         * @param to 拷貝路徑
         */
        fun copy(from: File, to: File) {
            if (!from.exists()) return
            copy(FileInputStream(from), to)
        }

        /**
         * 複製檔案
         *
         * @param from 來源
         * @param to 拷貝路徑
         */
        fun copy(from: InputStream, to: File) {
            mkdir(to.parentFile!!)
            FileOutputStream(to).use { outputStream ->
                from.copyTo(outputStream)
            }
        }

        fun writeBytesFile(dirPath: String, fileName: String, saveData: ByteArray) {
            writeBytesFile(File(dirPath), fileName, saveData)
        }

        fun writeBytesFile(dir: File, fileName: String, saveData: ByteArray) {
            mkdir(dir)
            val file = File(dir, fileName)
            file.writeBytes(saveData)
        }

        /**
         * 將文字輸出檔案
         *
         * @param dirPath 輸出目錄
         * @param fileName 輸出文字名稱
         * @param saveData 要儲存的檔案
         */
        fun writeTextFile(dirPath: String, fileName: String, saveData: String) {
            writeTextFile(File(dirPath), fileName, saveData)
        }

        /**
         * 將文字輸出檔案
         *
         * @param dir 輸出目錄
         * @param fileName 輸出文字名稱
         * @param saveData 要儲存的檔案
         */
        fun writeTextFile(dir: File, fileName: String, saveData: String) {
            mkdir(dir)
            val file = File(dir, fileName)
            FileOutputStream(file).use { outputStream ->
                OutputStreamWriter(outputStream, StandardCharsets.UTF_8).use { writer ->
                    writer.append(saveData)
                }
            }
        }

        /**
         * 讀取文字檔
         *
         * @param filePath 檔案路徑
         * @return 讀取資料
         */
        fun readText(filePath: String): String {
            return File(filePath).readText()
        }

        /**
         * 讀取文字檔
         *
         * @return 讀取資料
         */
        fun File.readText(): String {
            if (!this.exists() || this.isDirectory) return ""
            val stringBuilder = StringBuilder()
            BufferedReader(FileReader(this)).use { bufferedReader ->
                {
                    var str = bufferedReader.readLine()
                    while (str != null) {
                        stringBuilder.append(str)
                        str = bufferedReader.readLine()
                    }
                }
            }
            return stringBuilder.toString()
        }

        fun ByteArray.fileSizeText(): String {
            return this.size.toDouble().fileSizeText()
        }

        fun File.fileSizeText(): String {
            return this.length().toDouble().fileSizeText()
        }

        fun Double.fileSizeText(): String {
            val cntSize: String

            val sizeKb = this / 1024
            val sizeMb = sizeKb / 1024
            val sizeGb = sizeMb / 1024

            val decimalFormat = DecimalFormat("###.##")

            cntSize = if (sizeGb > 0) {
                decimalFormat.format(sizeGb) + "GB"
            } else if (sizeMb > 0) {
                decimalFormat.format(sizeMb) + "MB"
            } else {
                decimalFormat.format(sizeKb) + "KB"
            }

            return cntSize
        }

        fun File.mimeType(): String {
            val encoded: String = try {
                URLEncoder.encode(name, "UTF-8").replace("+", "%20")
            } catch (e: Exception) {
                name
            }
            return MimeTypeMap.getFileExtensionFromUrl(encoded).lowercase(Locale.getDefault())
        }

        fun mimeType(fileName: String): String {
            val mime = MimeTypeMap.getSingleton()
            val index: Int = fileName.lastIndexOf('.') + 1
            val ext: String = fileName.substring(index).lowercase(Locale.ENGLISH)
            return mime.getMimeTypeFromExtension(ext)!!
        }
    }
}
