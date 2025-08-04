package org.kevinparks.emulator

class Memory(private val rom: UByteArray) {
    private val ram = UByteArray(4096)

    fun readByte(address: UShort, fromROM: Boolean): UByte {
        val index = address.toInt()
        return if (fromROM) rom[index] else ram[index]
    }

    fun writeByte(address: UShort, value: UByte, toROM: Boolean) {
        if (toROM) throw IllegalAccessError("Cannot write to ROM")
        ram[address.toInt()] = value
    }

    fun readROMInstruction(addr: Int): UShort {
        val high = rom[addr].toInt() shl 8
        val low = rom[addr + 1].toInt()
        return (high or low).toUShort()
    }
}
