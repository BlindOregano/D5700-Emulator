package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.ConvertByteToAsciiInstruction

class ConvertByteToAsciiInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testConvertDigit0ToAscii() {
        val cpu = makeCPU()
        cpu.registers[1] = 0u

        // Convert r1 → r2 (0xE120)
        val instr = ConvertByteToAsciiInstruction(0xE120.toUShort())
        instr.execute(cpu)

        assertEquals('0'.code.toUByte(), cpu.registers[2])
    }

    @Test
    fun testConvertDigit9ToAscii() {
        val cpu = makeCPU()
        cpu.registers[0] = 9u

        val instr = ConvertByteToAsciiInstruction(0xE010.toUShort()) // r0 → r1
        instr.execute(cpu)

        assertEquals('9'.code.toUByte(), cpu.registers[1])
    }

    @Test
    fun testConvertDigitAtoFToAscii() {
        val cpu = makeCPU()
        for (hex in 0xA..0xF) {
            cpu.registers[2] = hex.toUByte()
            val instr = ConvertByteToAsciiInstruction(0xE230.toUShort()) // r2 → r3
            instr.execute(cpu)
            val expected = ('A'.code + (hex - 10)).toUByte()
            assertEquals(expected, cpu.registers[3])
        }
    }

    @Test
    fun testThrowsOnInvalidInputOverF() {
        val cpu = makeCPU()
        cpu.registers[4] = 0x10.toUByte()  // 16

        val instr = ConvertByteToAsciiInstruction(0xE450.toUShort()) // r4 → r5

        val ex = assertThrows(IllegalArgumentException::class.java) {
            instr.execute(cpu)
        }
        assertEquals("Cannot convert > 0xF to ASCII", ex.message)
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = ConvertByteToAsciiInstruction(0xE000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
