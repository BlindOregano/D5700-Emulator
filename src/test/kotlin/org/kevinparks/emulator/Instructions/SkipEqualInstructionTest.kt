package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SkipEqualInstruction

class SkipEqualInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testSkipsIfRegistersAreEqual() {
        val cpu = makeCPU()
        cpu.registers[1] = 10u
        cpu.registers[2] = 10u
        cpu.programCounter = 100u

        val instr = SkipEqualInstruction(0x8120.toUShort()) // if r1 == r2 → skip
        instr.execute(cpu)

        assertEquals(102u.toUShort(), cpu.programCounter)
    }

    @Test
    fun testDoesNotSkipIfRegistersAreNotEqual() {
        val cpu = makeCPU()
        cpu.registers[1] = 10u
        cpu.registers[2] = 20u
        cpu.programCounter = 200u

        val instr = SkipEqualInstruction(0x8120.toUShort()) // r1 != r2
        instr.execute(cpu)

        assertEquals(200u.toUShort(), cpu.programCounter) // stays put — CPU adds +2 later
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SkipEqualInstruction(0x8000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
