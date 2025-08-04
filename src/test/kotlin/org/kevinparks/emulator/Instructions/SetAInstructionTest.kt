package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.SetAInstruction

class SetAInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testSetsAddressCorrectly() {
        val cpu = makeCPU()

        val instr = SetAInstruction(0xA2FF.toUShort()) // A = 0x2FF
        instr.execute(cpu)

        assertEquals(0x2FF.toUShort(), cpu.address)
    }

    @Test
    fun testSetsMaxAddress() {
        val cpu = makeCPU()

        val instr = SetAInstruction(0xAFFF.toUShort()) // A = 0x0FFF
        instr.execute(cpu)

        assertEquals(0x0FFF.toUShort(), cpu.address)
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = SetAInstruction(0xA000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
