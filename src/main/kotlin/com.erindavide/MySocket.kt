package com.erindavide

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.charset.Charset

object MySocket{

    fun create() {
        val PORT = Integer.parseInt(System.getenv("PORT") ?: "3000")

        val socket = ServerSocket()
        socket.bind(InetSocketAddress(InetAddress.getLocalHost(), PORT))


        while (true) {
            val conn = socket.accept()
            while (conn.inputStream.available() > 0) {
                conn.inputStream.read()
            }

            conn.outputStream.write("200 OK\n".toByteArray(Charset.defaultCharset()))

            conn.outputStream.flush()
            conn.outputStream.close()
            conn.close()
        }
    }
}