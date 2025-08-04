package org.kevinparks.emulator.Display

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.*
import org.kevinparks.emulator.display.Display

class D5700CPUTest {

    private lateinit var memory: Memory
    private lateinit var cpu: D5700CPU

    @BeforeEach
    fun setup() {
        // Create dummy ROM (filled with NOP-like instructions or safe operations)
        val rom = UByteArray(4096) { 0u }
        memory = Memory(rom)
        cpu = D5700CPU(memory, Display())
    }

    @Test
    fun testInitialState() {
        cpu.reset()

        println("programCounter: ${cpu.programCounter}")
        println("timer: ${cpu.timer}")
        println("address: ${cpu.address}")
        println("memoryFlag: ${cpu.memoryFlag}")
        println("registers: ${cpu.registers.joinToString()}")

        assertEquals(0.toUShort(), cpu.programCounter)
        assertEquals(0u.toUByte(), cpu.timer)
        assertEquals(0u.toUShort(), cpu.address)
        assertFalse(cpu.memoryFlag)
        assertTrue(cpu.registers.all { it == 0.toUByte() })
    }


    @Test
    fun testResetClearsAllRegistersAndState() {
        cpu.registers[0] = 42u
        cpu.programCounter = 100u
        cpu.timer = 10u
        cpu.address = 0x1234u
        cpu.memoryFlag = true

        cpu.reset()

        assertEquals(0u.toUShort(), cpu.programCounter)
        assertEquals(0u.toUByte(), cpu.timer)
        assertEquals(0u.toUShort(), cpu.address)
        assertFalse(cpu.memoryFlag)
        assertTrue(cpu.registers.all { it == 0.toUByte() })
    }

    @Test
    fun testStepRunsInstructionAndAdvancesPC() {
        val rom = UByteArray(4096) { 0u }
        val instruction: UShort = 0x1010u // ADD r0 + r1 → r0
        rom[0] = (instruction.toInt() shr 8).toUByte()
        rom[1] = (instruction.toInt() and 0xFF).toUByte()

        memory = Memory(rom)
        cpu = D5700CPU(memory, Display())
        cpu.registers[0] = 5u
        cpu.registers[1] = 7u

        cpu.step()

        assertEquals(12u.toUByte(), cpu.registers[0])
        assertEquals(2u.toUShort(), cpu.programCounter)
    }


    @Test
    fun testStepDoesNotAdvancePCIfFlagFalse() {
        // Simulate a JUMP instruction that doesn't advance PC
        val jumpInstruction = 0x5000.toUShort()
        memory = Memory(UByteArray(4096).apply {
            this[0] = (jumpInstruction.toInt() shr 8).toUByte()
            this[1] = (jumpInstruction.toInt() and 0xFF).toUByte()
        })
        cpu = D5700CPU(memory, Display())

        cpu.step()

        assertEquals(0x0000.toUShort(), cpu.programCounter) // Should remain unchanged unless manually set
    }

    @Test
    fun testHasTimerChanged() {
        cpu.timer = 5u
        assertTrue(cpu.hasTimerChanged())
        assertFalse(cpu.hasTimerChanged()) // no change
        cpu.timer = 4u
        assertTrue(cpu.hasTimerChanged())  // change detected
    }
}
