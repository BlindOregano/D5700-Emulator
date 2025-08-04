package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SkipNotEqualInstruction

class SkipNotEqualInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testSkipsIfRegistersAreNotEqual() {
        val cpu = makeCPU()
        cpu.registers[1] = 5u
        cpu.registers[2] = 9u
        cpu.programCounter = 300u

        val instr = SkipNotEqualInstruction(0x9120.toUShort()) // r1 != r2 → skip
        instr.execute(cpu)

        assertEquals(302u.toUShort(), cpu.programCounter)
    }

    @Test
    fun testDoesNotSkipIfRegistersAreEqual() {
        val cpu = makeCPU()
        cpu.registers[3] = 42u
        cpu.registers[4] = 42u
        cpu.programCounter = 400u

        val instr = SkipNotEqualInstruction(0x9340.toUShort()) // r3 == r4
        instr.execute(cpu)

        assertEquals(400u.toUShort(), cpu.programCounter)
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SkipNotEqualInstruction(0x9000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
