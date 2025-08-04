package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class ReadTimerInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        cpu.registers[rX] = cpu.timer
    }
}
