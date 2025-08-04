package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SubInstruction

class SubInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testSimpleSubtraction() {
        val cpu = makeCPU()
        cpu.registers[0] = 20u
        cpu.registers[1] = 5u

        val instr = SubInstruction(0x2012.toUShort()) // r0 - r1 → r2
        instr.execute(cpu)

        assertEquals(15.toUByte(), cpu.registers[2])
    }

    @Test
    fun testSubtractionWrapsAround() {
        val cpu = makeCPU()
        cpu.registers[1] = 5u
        cpu.registers[2] = 10u

        val instr = SubInstruction(0x2120.toUShort()) // r1 - r2 → r0 = 5 - 10 = -5 → 251
        instr.execute(cpu)

        assertEquals(251.toUByte(), cpu.registers[0])
    }

    @Test
    fun testZeroResult() {
        val cpu = makeCPU()
        cpu.registers[3] = 77u
        cpu.registers[4] = 77u

        val instr = SubInstruction(0x2345.toUShort()) // r3 - r4 → r5
        instr.execute(cpu)

        assertEquals(0.toUByte(), cpu.registers[5])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SubInstruction(0x2000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
