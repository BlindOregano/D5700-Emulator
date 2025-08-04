package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class SetTimerInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val value = ((raw.toInt() shr 8) and 0xFF).toUByte()
        cpu.timer = value
    }
}
