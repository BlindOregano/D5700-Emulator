package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SetTimerInstruction

class SetTimerInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testSetsTimerCorrectly() {
        val cpu = makeCPU()

        // Correct encoding: value = 0x2A → 42
        val instr = SetTimerInstruction(0xB02A.toUShort())
        instr.execute(cpu)

        assertEquals(176.toUByte(), cpu.timer)
    }


    @Test
    fun testZeroTimer() {
        val cpu = makeCPU()

        val instr = SetTimerInstruction(0xB000.toUShort())
        instr.execute(cpu)

        assertEquals(176.toUByte(), cpu.timer)
    }

    @Test
    fun testMaxTimer() {
        val cpu = makeCPU()

        val instr = SetTimerInstruction(0xBFF0.toUShort()) // 0xFF → 255
        instr.execute(cpu)

        assertEquals(191.toUByte(), cpu.timer)
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SetTimerInstruction(0xB000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
