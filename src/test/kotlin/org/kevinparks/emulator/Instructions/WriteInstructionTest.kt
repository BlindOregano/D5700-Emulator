package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.WriteInstruction

class WriteInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testWritesToRAM() {
        val cpu = makeCPU()
        cpu.registers[2] = 123u
        cpu.address = 500u
        cpu.memoryFlag = false

        val instr = WriteInstruction(0x4200.toUShort()) // write r2 → A
        instr.execute(cpu)

        val actual = cpu.memory.readByte(500u, fromROM = false)
        assertEquals(123.toUByte(), actual)
    }

    @Test
    fun testThrowsWhenWritingToROM() {
        val cpu = makeCPU()
        cpu.registers[1] = 99u
        cpu.address = 42u
        cpu.memoryFlag = true // ROM

        val instr = WriteInstruction(0x4100.toUShort()) // write r1 → A (ROM)

        val ex = assertThrows<IllegalAccessError> {
            instr.execute(cpu)
        }

        assertTrue(ex.message!!.contains("Cannot write to ROM"))
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = WriteInstruction(0x4000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
