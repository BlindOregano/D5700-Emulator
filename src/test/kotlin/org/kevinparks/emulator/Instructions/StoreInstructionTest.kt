package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.StoreInstruction

class StoreInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testStoresByteIntoRegister() {
        val cpu = makeCPU()

        // r1 = 0x7F (127)
        val instr = StoreInstruction(0x017F.toUShort())
        instr.execute(cpu)

        assertEquals(127.toUByte(), cpu.registers[1])
    }

    @Test
    fun testStoresZero() {
        val cpu = makeCPU()

        // r3 = 0
        val instr = StoreInstruction(0x0300.toUShort())
        instr.execute(cpu)

        assertEquals(0.toUByte(), cpu.registers[3])
    }

    @Test
    fun testStoresMaxByte() {
        val cpu = makeCPU()

        // r4 = 0xFF (255)
        val instr = StoreInstruction(0x04FF.toUShort())
        instr.execute(cpu)

        assertEquals(255.toUByte(), cpu.registers[4])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = StoreInstruction(0x0000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
