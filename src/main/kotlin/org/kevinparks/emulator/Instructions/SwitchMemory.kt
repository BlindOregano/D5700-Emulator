package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class SwitchMemoryInstruction : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        cpu.memoryFlag = !cpu.memoryFlag
    }
}
