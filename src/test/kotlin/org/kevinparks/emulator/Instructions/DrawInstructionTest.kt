package org.kevinparks.emulator.Instructions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.D5700CPU
import org.kevinparks.emulator.Memory
import org.kevinparks.emulator.display.Display
import org.kevinparks.emulator.instructions.DrawInstruction

class DrawInstructionTest {

    private fun makeCPU(): D5700CPU {
        val rom = UByteArray(4096) { 0u }
        return D5700CPU(Memory(rom), Display())
    }

    @Test
    fun testDrawAsciiCharacterAtValidPosition() {
        val cpu = makeCPU()
        cpu.registers[1] = '@'.code.toUByte() // ASCII 64
        val row = 3
        val col = 2

        // opcode F, rX=1, rY=3, rZ=2 → 0xF132
        val instr = DrawInstruction(0xF132.toUShort())
        instr.execute(cpu)

        val index = row * 8 + col
        val framebufferField = cpu.display.javaClass.getDeclaredField("framebuffer")
        framebufferField.isAccessible = true

        val rawByteArray = framebufferField.get(cpu.display) as ByteArray
        val framebuffer = rawByteArray.asUByteArray()

        assertEquals('@'.code.toUByte(), framebuffer[26])
    }

    @Test
    fun testThrowsOnAsciiOver127() {
        val cpu = makeCPU()
        cpu.registers[0] = 0x80.toUByte() // 128, invalid

        val instr = DrawInstruction(0xF000.toUShort()) // r0 → (0,0)
        val ex = assertThrows(IllegalArgumentException::class.java) {
            instr.execute(cpu)
        }

        assertTrue(ex.message!!.contains("Invalid ASCII byte"))
    }

    @Test
    fun testThrowsOnRowOutOfBounds() {
        val cpu = makeCPU()
        cpu.registers[2] = '#'.code.toUByte()

        // row = 8 (out of bounds), col = 0 → rX=2, rY=8, rZ=0 = 0xF280
        val instr = DrawInstruction(0xF280.toUShort())
        val ex = assertThrows(IndexOutOfBoundsException::class.java) {
            instr.execute(cpu)
        }

        assertTrue(ex.message!!.contains("out of bounds"))
    }

    @Test
    fun testThrowsOnColOutOfBounds() {
        val cpu = makeCPU()
        cpu.registers[3] = '%'.code.toUByte()

        // row = 7, col = 8 (out of bounds) → rX=3, rY=7, rZ=8 = 0xF378
        val instr = DrawInstruction(0xF378.toUShort())
        val ex = assertThrows(IndexOutOfBoundsException::class.java) {
            instr.execute(cpu)
        }

        assertTrue(ex.message!!.contains("out of bounds"))
    }

    @Test
    fun testShouldAdvancePCIsTrue() {
        val instr = DrawInstruction(0xF000.toUShort())
        assertTrue(instr.shouldAdvancePC)
    }
}
