package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.ReadTimerInstruction

class ReadTimerInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testReadsTimerIntoRegister() {
        val cpu = makeCPU()
        cpu.timer = 77.toUByte()

        val instr = ReadTimerInstruction(0xC200.toUShort()) // r2 = timer
        instr.execute(cpu)

        assertEquals(77.toUByte(), cpu.registers[2])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = ReadTimerInstruction(0xC000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
