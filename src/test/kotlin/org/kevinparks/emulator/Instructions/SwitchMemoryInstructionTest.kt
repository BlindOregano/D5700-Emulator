package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SwitchMemoryInstruction

class SwitchMemoryInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testTogglesFromFalseToTrue() {
        val cpu = makeCPU()
        cpu.memoryFlag = false

        val instr = SwitchMemoryInstruction()
        instr.execute(cpu)

        assertTrue(cpu.memoryFlag)
    }

    @Test
    fun testTogglesFromTrueToFalse() {
        val cpu = makeCPU()
        cpu.memoryFlag = true

        val instr = SwitchMemoryInstruction()
        instr.execute(cpu)

        assertFalse(cpu.memoryFlag)
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SwitchMemoryInstruction()
        assertTrue(instr.shouldAdvancePC)
    }
}
