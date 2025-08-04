package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.JumpInstruction

class JumpInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testValidJumpSetsProgramCounter() {
        val cpu = makeCPU()
        assertEquals(0.toUShort(), cpu.programCounter)

        val instr = JumpInstruction(0x51F2.toUShort()) // Jump to 0x1F2 (even)
        instr.execute(cpu)

        assertEquals(0x1F2.toUShort(), cpu.programCounter)
    }

    @Test
    fun testJumpToZero() {
        val cpu = makeCPU()
        cpu.programCounter = 100u

        val instr = JumpInstruction(0x5000.toUShort()) // Jump to 0x000
        instr.execute(cpu)

        assertEquals(0.toUShort(), cpu.programCounter)
    }

    @Test
    fun testThrowsIfAddressIsOdd() {
        val cpu = makeCPU()
        val instr = JumpInstruction(0x51F3.toUShort()) // 0x1F3 is odd

        val ex = assertThrows(IllegalArgumentException::class.java) {
            instr.execute(cpu)
        }

        assertTrue(ex.message!!.contains("must be even"))
    }

    @Test
    fun testShouldAdvancePCIsFalse() {
        val instr = JumpInstruction(0x5000.toUShort())
        assertFalse(instr.shouldAdvancePC)
    }
}
