package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class StoreInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() ushr 8) and 0xF
        val bb = (raw.toInt() and 0xFF).toUByte()
        cpu.registers[rX] = bb
    }
}
