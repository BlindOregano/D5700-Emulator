package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.AddInstruction

class AddInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        val memory = Memory(rom)
        return D5700CPU(memory, Display())
    }

    @Test
    fun testSimpleAddition() {
        val cpu = makeCPU()
        cpu.registers[1] = 10u
        cpu.registers[2] = 20u

        // Encoded: ADD r1 + r2 → r0 = 0x1120
        val instruction = AddInstruction(0x1120.toUShort())
        instruction.execute(cpu)

        assertEquals(30.toUByte(), cpu.registers[0])
    }

    @Test
    fun testRegisterOverwrite() {
        val cpu = makeCPU()
        cpu.registers[3] = 5u
        cpu.registers[4] = 8u

        // ADD r3 + r4 → r4 = 0x1344
        val instruction = AddInstruction(0x1344.toUShort())
        instruction.execute(cpu)

        assertEquals(13.toUByte(), cpu.registers[4])
    }

    @Test
    fun testOverflowWraparound() {
        val cpu = makeCPU()
        cpu.registers[5] = 250.toUByte()
        cpu.registers[6] = 10.toUByte()

        // ADD r5 + r6 → r7 = 0x1567
        val instruction = AddInstruction(0x1567.toUShort())
        instruction.execute(cpu)

        // 250 + 10 = 260 → wraps to 4
        assertEquals(4.toUByte(), cpu.registers[7])
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instruction = AddInstruction(0x1234.toUShort())
        assertEquals(true, instruction.shouldAdvancePC)
    }
}
