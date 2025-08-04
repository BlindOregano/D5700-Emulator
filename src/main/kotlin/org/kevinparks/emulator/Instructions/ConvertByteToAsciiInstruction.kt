package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class ConvertByteToAsciiInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val rY = (raw.toInt() shr 4) and 0xF

        val value = cpu.registers[rX].toInt()
        if (value > 0xF) throw IllegalArgumentException("Cannot convert > 0xF to ASCII")

        val ascii = if (value < 10) '0' + value else 'A' + (value - 10)
        cpu.registers[rY] = ascii.code.toUByte()
    }
}
