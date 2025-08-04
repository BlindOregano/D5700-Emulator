package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class ReadInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val value = cpu.memory.readByte(cpu.address, cpu.memoryFlag)
        cpu.registers[rX] = value
    }
}
