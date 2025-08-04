package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class DrawInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val rY = (raw.toInt() shr 4) and 0xF
        val rZ = raw.toInt() and 0xF

        val ascii = cpu.registers[rX].toInt()
        if (ascii > 0x7F) throw IllegalArgumentException("Invalid ASCII byte: $ascii")

        val row = rY
        val col = rZ

        println("DRAW: ascii=${ascii}, row=${row}, col=${col}") // 👈 DEBUG PRINT

        if (row !in 0..7 || col !in 0..7) throw IndexOutOfBoundsException("Screen coords out of bounds")

        val addr = row * 8 + col
        cpu.display.setCharAt(addr, ascii.toUByte())
    }
}
