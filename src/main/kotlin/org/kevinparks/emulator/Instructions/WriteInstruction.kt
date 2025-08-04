package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class WriteInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val value = cpu.registers[rX]
        cpu.memory.writeByte(cpu.address, value, cpu.memoryFlag)
    }
}
