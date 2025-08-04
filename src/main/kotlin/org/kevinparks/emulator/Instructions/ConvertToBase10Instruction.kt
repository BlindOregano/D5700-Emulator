package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class ConvertToBase10Instruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val value = cpu.registers[rX].toInt()

        val hundreds = (value / 100).toUByte()
        val tens = ((value % 100) / 10).toUByte()
        val ones = (value % 10).toUByte()

        val a = cpu.address.toInt()
        cpu.memory.writeByte(a.toUShort(), hundreds, false)
        cpu.memory.writeByte((a + 1).toUShort(), tens, false)
        cpu.memory.writeByte((a + 2).toUShort(), ones, false)
    }
}
