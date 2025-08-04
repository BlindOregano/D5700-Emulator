package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class SubInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val rY = (raw.toInt() shr 4) and 0xF
        val rZ = raw.toInt() and 0xF

        val result = cpu.registers[rX] - cpu.registers[rY]
        cpu.registers[rZ] = result.toUByte()  // wraps naturally
    }
}
