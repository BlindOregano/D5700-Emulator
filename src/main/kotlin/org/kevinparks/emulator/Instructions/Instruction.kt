package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

interface Instruction {
    val shouldAdvancePC: Boolean
    fun execute(cpu: D5700CPU)
}
