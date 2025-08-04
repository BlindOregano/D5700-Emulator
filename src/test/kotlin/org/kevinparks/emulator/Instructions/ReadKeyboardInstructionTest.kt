package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.ReadKeyboardInstruction
import java.io.ByteArrayInputStream

class ReadKeyboardInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    private fun simulateInput(input: String) {
        System.setIn(ByteArrayInputStream(input.toByteArray()))
    }

    @Test
    fun testValidHexInput() {
        simulateInput("2A\n") // hex 2A = 42

        val cpu = makeCPU()
        val instr = ReadKeyboardInstruction(0x6300.toUShort()) // r3
        instr.execute(cpu)

        assertEquals(42.toUByte(), cpu.registers[3])
    }

    @Test
    fun testInputEmptyDefaultsToZero() {
        simulateInput("\n")

        val cpu = makeCPU()
        val instr = ReadKeyboardInstruction(0x6100.toUShort()) // r1
        instr.execute(cpu)

        assertEquals(0.toUByte(), cpu.registers[1])
    }

    @Test
    fun testInputInvalidDefaultsToZero() {
        simulateInput("GZ\n")

        val cpu = makeCPU()
        val instr = ReadKeyboardInstruction(0x6200.toUShort()) // r2
        instr.execute(cpu)

        assertEquals(0.toUByte(), cpu.registers[2])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = ReadKeyboardInstruction(0x6000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
