package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class JumpInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = false // jump sets PC directly

    override fun execute(cpu: D5700CPU) {
        val aaa = raw.toInt() and 0x0FFF
        if (aaa % 2 != 0) {
            throw IllegalArgumentException("JUMP address must be even: $aaa")
        }
        cpu.programCounter = aaa.toUShort()
    }
}
