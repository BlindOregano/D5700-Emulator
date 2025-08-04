package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.ConvertToBase10Instruction

class ConvertToBase10InstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testConvertSingleDigit() {
        val cpu = makeCPU()
        cpu.registers[2] = 7u
        cpu.address = 100u

        val instr = ConvertToBase10Instruction(0xD200.toUShort()) // r2
        instr.execute(cpu)

        val mem = cpu.memory
        assertEquals(0u.toUByte(), mem.readByte(100u, fromROM = false))     // hundreds
        assertEquals(0u.toUByte(), mem.readByte(101u, fromROM = false))    // tens
        assertEquals(7u.toUByte(), mem.readByte(102u, fromROM = false))    // ones
    }

    @Test
    fun testConvertTwoDigit() {
        val cpu = makeCPU()
        cpu.registers[1] = 42u
        cpu.address = 200u

        val instr = ConvertToBase10Instruction(0xD100.toUShort()) // r1
        instr.execute(cpu)

        assertEquals(0u.toUByte(), cpu.memory.readByte(200u, false))       // hundreds
        assertEquals(4u.toUByte(), cpu.memory.readByte(201u, false))       // tens
        assertEquals(2u.toUByte(), cpu.memory.readByte(202u, false))       // ones
    }

    @Test
    fun testConvertThreeDigit() {
        val cpu = makeCPU()
        cpu.registers[0] = 255u
        cpu.address = 4090u

        val instr = ConvertToBase10Instruction(0xD000.toUShort()) // r0
        instr.execute(cpu)

        assertEquals(2u.toUByte(), cpu.memory.readByte(4090u, false))
        assertEquals(5u.toUByte(), cpu.memory.readByte(4091u, false))
        assertEquals(5u.toUByte(), cpu.memory.readByte(4092u, false))
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = ConvertToBase10Instruction(0xD000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
