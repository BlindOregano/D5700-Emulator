package org.kevinparks.emulator.instructions

import org.kevinparks.emulator.D5700CPU

class SkipEqualInstruction(private val raw: UShort) : Instruction {
    override val shouldAdvancePC = true

    override fun execute(cpu: D5700CPU) {
        val rX = (raw.toInt() shr 8) and 0xF
        val rY = (raw.toInt() shr 4) and 0xF

        if (cpu.registers[rX] == cpu.registers[rY]) {
            cpu.programCounter = (cpu.programCounter + 2u).toUShort() // skip next instruction
        }
    }
}
