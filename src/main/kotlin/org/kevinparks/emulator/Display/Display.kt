package org.kevinparks.emulator.display

class Display {
    private val framebuffer = UByteArray(64) { ' '.code.toUByte() }

    fun setCharAt(index: Int, value: UByte) {
        framebuffer[index] = value
    }

    fun render() {
        for (i in 0 until 64 step 8) {
            val row = framebuffer.slice(i until i + 8)
                .map { it.toInt().toChar() }
                .joinToString("")
            println(row)
        }
    }

    fun drawChar(char: UByte, row: Int, col: Int) {
        if (row !in 0..7 || col !in 0..7) return  // bounds check
        val index = row * 8 + col
        framebuffer[index] = char
    }
}
