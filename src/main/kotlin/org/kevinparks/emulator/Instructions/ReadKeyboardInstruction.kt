package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class ReadKeyboardInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        print("Input (0–FF hex): ")
        val input = readLine()?.take(2)?.uppercase() ?: ""
        val value = input.toIntOrNull(16) ?: 0
        cpu.registers[rX] = value.toUByte()
    }
}
