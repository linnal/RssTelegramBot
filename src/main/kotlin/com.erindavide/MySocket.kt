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
            println(" === before socket accept connection === $PORT")
            val conn = socket.accept()
            println(" === after socket accept socket connection === $PORT")
            while (conn.inputStream.available() > 0) {
                conn.inputStream.read()
            }
            println(" === conn write === $PORT")

            conn.outputStream.write("200 OK\n".toByteArray(Charset.defaultCharset()))
            println(" === conn flush === $PORT")

            conn.outputStream.flush()
            println(" === conn out stream close === $PORT")
            conn.outputStream.close()
            println(" === conn  close === $PORT")
            conn.close()
        }
    }
}