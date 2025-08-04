package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class SetAInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val aaa = raw.toInt() and 0x0FFF
        if (aaa > 0x0FFF) throw IllegalArgumentException("Address out of range: $aaa")
        cpu.address = aaa.toUShort()
    }
}
