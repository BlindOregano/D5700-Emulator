package org.kevinparks.emulator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kevinparks.emulator.Memory

class MemoryTest {

    private var rom: UByteArray = UByteArray(0)
    private lateinit var memory: Memory

    @BeforeEach
    fun setUp() {
        rom = UByteArray(4096) { it.toUByte() }  // ROM filled with 0, 1, 2, ...
        memory = Memory(rom)
    }

    @Test
    fun testReadByteFromROM() {
        val address = 10.toUShort()
        val byte = memory.readByte(address, fromROM = true)
        assertEquals(10.toUByte(), byte)
    }

    @Test
    fun testReadByteFromRAM() {
        val address = 123.toUShort()
        memory.writeByte(address, 42.toUByte(), toROM = false)
        val byte = memory.readByte(address, fromROM = false)
        assertEquals(42.toUByte(), byte)
    }

    @Test
    fun testWriteByteToROMThrows() {
        val address = 456.toUShort()
        assertThrows(IllegalAccessError::class.java) {
            memory.writeByte(address, 99.toUByte(), toROM = true)
        }
    }

    @Test
    fun testReadROMInstructionCombinesTwoBytes() {
        // address 20 = 0x14 = 20, 21 = 0x15 = 21
        // Expected result: (0x14 << 8) | 0x15 = 0x1415
        val instruction = memory.readROMInstruction(20)
        assertEquals(0x1415.toUShort(), instruction)
    }
}
