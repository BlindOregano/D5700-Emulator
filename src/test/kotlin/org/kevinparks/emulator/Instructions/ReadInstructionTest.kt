package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.ReadInstruction

class ReadInstructionTest {

    private fun makeCPU(withROM: UByteArray = UByteArray(4096) { 0u }): D5700CPU {
        return D5700CPU(Memory(withROM), Display())
    }

    @Test
    fun testReadFromRAMIntoRegister() {
        val cpu = makeCPU()
        cpu.memory.writeByte(123u, 42u, toROM = false)
        cpu.address = 123u
        cpu.memoryFlag = false

        val instr = ReadInstruction(0x3400.toUShort()) // r4 = mem[A]
        instr.execute(cpu)

        assertEquals(42u.toUByte(), cpu.registers[4])
    }

    @Test
    fun testReadFromROMIntoRegister() {
        val rom = UByteArray(4096) { 0u }.apply { this[99] = 88u }
        val cpu = makeCPU(rom)
        cpu.address = 99u
        cpu.memoryFlag = true

        val instr = ReadInstruction(0x3200.toUShort()) // r2 = mem[A]
        instr.execute(cpu)

        assertEquals(88u.toUByte(), cpu.registers[2])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = ReadInstruction(0x3000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
